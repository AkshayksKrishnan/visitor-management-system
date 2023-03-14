package io.bootify.l11_visitor_management.repos;

import io.bootify.l11_visitor_management.domain.Visitor;
import io.bootify.l11_visitor_management.model.VisitorDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    public Optional<Visitor> findByIdNum(String idNum);
}
