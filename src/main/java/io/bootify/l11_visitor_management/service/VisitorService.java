package io.bootify.l11_visitor_management.service;

import io.bootify.l11_visitor_management.domain.Address;
import io.bootify.l11_visitor_management.domain.Visit;
import io.bootify.l11_visitor_management.domain.Visitor;
import io.bootify.l11_visitor_management.model.VisitorDTO;
import io.bootify.l11_visitor_management.repos.AddressRepository;
import io.bootify.l11_visitor_management.repos.VisitorRepository;
import io.bootify.l11_visitor_management.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final AddressRepository addressRepository;

    public VisitorService(final VisitorRepository visitorRepository,
            final AddressRepository addressRepository) {
        this.visitorRepository = visitorRepository;
        this.addressRepository = addressRepository;
    }

    public List<VisitorDTO> findAll() {
        final List<Visitor> visitors = visitorRepository.findAll(Sort.by("id"));
        return visitors.stream()
                .map((visitor) -> mapToDTO(visitor, new VisitorDTO()))
                .collect(Collectors.toList());
    }

    public VisitorDTO get(final Long id) {
        return visitorRepository.findById(id)
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    public VisitorDTO getByIdNum(String idNum) {
        return visitorRepository.findByIdNum(idNum)
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    public Long create(final VisitorDTO visitorDTO) {
        final Visitor visitor = new Visitor();
        mapToEntity(visitorDTO, visitor);
        return visitorRepository.save(visitor).getId();
    }


    public void update(final Long id, final VisitorDTO visitorDTO) {
        final Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(visitorDTO, visitor);
        visitorRepository.save(visitor);
    }

    public void delete(final Long id) {
        visitorRepository.deleteById(id);
    }

    private VisitorDTO mapToDTO(final Visitor visitor, final VisitorDTO visitorDTO) {
        visitorDTO.setId(visitor.getId());
        visitorDTO.setName(visitor.getName());
        visitorDTO.setPhone(visitor.getPhone());
        visitorDTO.setIdType(visitor.getIdType());
        visitorDTO.setIdNumber(visitor.getIdNum());
        visitorDTO.setAddress(visitor.getAddress() == null ? null : visitor.getAddress().getId());
        return visitorDTO;
    }

    private Visitor mapToEntity(final VisitorDTO visitorDTO, final Visitor visitor) {
        visitor.setName(visitorDTO.getName());
        visitor.setPhone(visitorDTO.getPhone());
        visitor.setIdType(visitorDTO.getIdType());
        visitor.setIdNum(visitorDTO.getIdNumber());
        final Address address = visitorDTO.getAddress() == null ? null : addressRepository.findById(visitorDTO.getAddress())
                .orElseThrow(() -> new NotFoundException("address not found"));
        visitor.setAddress(address);
        return visitor;
    }
}
