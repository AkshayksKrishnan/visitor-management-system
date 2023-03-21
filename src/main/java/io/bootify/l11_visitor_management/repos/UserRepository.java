package io.bootify.l11_visitor_management.repos;

import io.bootify.l11_visitor_management.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
