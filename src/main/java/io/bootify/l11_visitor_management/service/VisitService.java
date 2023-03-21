package io.bootify.l11_visitor_management.service;

import io.bootify.l11_visitor_management.domain.Flat;
import io.bootify.l11_visitor_management.domain.User;
import io.bootify.l11_visitor_management.domain.Visit;
import io.bootify.l11_visitor_management.domain.Visitor;
import io.bootify.l11_visitor_management.model.VisitDTO;
import io.bootify.l11_visitor_management.model.VisitStatus;
import io.bootify.l11_visitor_management.repos.FlatRepository;
import io.bootify.l11_visitor_management.repos.UserRepository;
import io.bootify.l11_visitor_management.repos.VisitRepository;
import io.bootify.l11_visitor_management.repos.VisitorRepository;
import io.bootify.l11_visitor_management.util.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class VisitService {

    @Autowired
    private  FlatRepository flatRepository;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private String pending_visits_prefix="pending_visits_";

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;

    public VisitService(final VisitRepository visitRepository,
                        final VisitorRepository visitorRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
    }

    public List<VisitDTO> findAll() {
        List<Visit> visits = visitRepository.findAll(Sort.by("id"));
        return visits.stream()
                .map((visit) -> mapToDTO(visit, new VisitDTO()))
                .collect(Collectors.toList());
    }

    public VisitDTO get(final Long id) {
        return visitRepository.findById(id)
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    public Long create(final VisitDTO visitDTO) {
        visitDTO.setStatus(VisitStatus.PENDING);
        final Visit visit = new Visit();
        mapToEntity(visitDTO, visit);
        return visitRepository.save(visit).getId();
    }

    public void markEntry(Long id) {

        Visit visit = visitRepository.findById(id).get();
        if(visit!=null && visit.getStatus().equals(VisitStatus.APPROVED)){
            visit.setInTime(LocalDateTime.now());
            visitRepository.save(visit);

        }
        else{
           throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status not updated");
        }
    }

    public void markExit(Long id) {
        Visit visit = visitRepository.findById(id).get();
        if(visit!=null && visit.getStatus().equals(VisitStatus.APPROVED)){
            visit.setOutTime(LocalDateTime.now());
            visit.setStatus(VisitStatus.COMPLETED);
            visitRepository.save(visit);

        }
        else{
          throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status not updated");
        }
    }

    @Transactional
    public void approveVisit(Long visitID,Long userID){

        Visit visit = visitRepository.findById(visitID).get();
        Flat flat = visit.getFlat();
        User user = userRepository.findById(userID).get();
        if(user.getFlat()==flat && visit.getStatus().equals(VisitStatus.PENDING)){
            visit.setStatus(VisitStatus.APPROVED);
            visit.setInTime(LocalDateTime.now());
            visitRepository.save(visit);
            String key = pending_visits_prefix+flat.getId();
            redisTemplate.delete(key);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status not updated");
        }
    }

    public List<VisitDTO> getPendingVisitsByUser(Long userID) {
        User user = userRepository.findById(userID).get();
        Flat userFlat = user.getFlat();
        String key = pending_visits_prefix+userFlat.getId();
        List<VisitDTO> visitDTOList = (List<VisitDTO>) redisTemplate.opsForValue().get(key);
        if (visitDTOList == null){
            visitDTOList = visitRepository.findByFlatAndStatus(userFlat,VisitStatus.PENDING)
                    .stream()
                    .map(visit -> mapToDTO(visit, new VisitDTO()))
                    .collect(Collectors.toList());
            redisTemplate.opsForValue().set(key,visitDTOList);
        }
        return  visitDTOList;
    }

    public void rejectVisit(Long visitID, Long userID) {
        Visit visit = visitRepository.findById(visitID).get();
        Flat flat = visit.getFlat();
        User user = userRepository.findById(userID).get();

        if(user.getFlat() == flat && visit.getStatus().equals(VisitStatus.PENDING)){
            visit.setStatus(VisitStatus.REJECTED);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status not updated");
        }
    }

    public void update(final Long id, final VisitDTO visitDTO) {
        final Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(visitDTO, visit);
        visitRepository.save(visit);
    }

    public void delete(final Long id) {
        visitRepository.deleteById(id);
    }

    private VisitDTO mapToDTO(final Visit visit, final VisitDTO visitDTO) {
        visitDTO.setId(visit.getId());
        visitDTO.setStatus(visit.getStatus());
        visitDTO.setInTime(visit.getInTime());
        visitDTO.setOutTime(visit.getOutTime());
        visitDTO.setPurpose(visit.getPurpose());
        visitDTO.setUrlOfImage(visit.getUrlOfImage());
        visitDTO.setNoOfPeople(visit.getNoOfPeople());
        visitDTO.setVisitor(visit.getVisitor() == null ? null : visit.getVisitor().getId());
        visitDTO.setFlat(visit.getFlat() == null ? null : visit.getFlat().getId());
        return visitDTO;
    }

    private Visit mapToEntity(final VisitDTO visitDTO, final Visit visit) {
        visit.setStatus(visitDTO.getStatus());
        visit.setInTime(visitDTO.getInTime());
        visit.setOutTime(visitDTO.getOutTime());
        visit.setPurpose(visitDTO.getPurpose());
        visit.setUrlOfImage(visitDTO.getUrlOfImage());
        visit.setNoOfPeople(visitDTO.getNoOfPeople());
        final Visitor visitor = visitDTO.getVisitor() == null ? null : visitorRepository.findById(visitDTO.getVisitor())
                .orElseThrow(() -> new NotFoundException("visitor not found"));
        visit.setVisitor(visitor);
        final Flat flat = visitDTO.getFlat() == null ? null : flatRepository.findById(visitDTO.getFlat())
                .orElseThrow(() -> new NotFoundException("flat not found"));
        visit.setFlat(flat);
        return visit;
    }

}
