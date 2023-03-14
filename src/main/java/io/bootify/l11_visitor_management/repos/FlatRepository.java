package io.bootify.l11_visitor_management.repos;

import io.bootify.l11_visitor_management.domain.Flat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlatRepository extends JpaRepository<Flat, Long> {
}
