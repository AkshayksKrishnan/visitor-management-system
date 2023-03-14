package io.bootify.l11_visitor_management.repos;

import io.bootify.l11_visitor_management.domain.Flat;
import io.bootify.l11_visitor_management.domain.Visit;
import io.bootify.l11_visitor_management.model.VisitDTO;
import io.bootify.l11_visitor_management.model.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {
//    List<Visit> findByFlat(Flat flat, VisitStatus pending);

    public List<Visit> findByFlatAndStatus(Flat flat, VisitStatus status);

}
