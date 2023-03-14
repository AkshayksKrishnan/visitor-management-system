package io.bootify.l11_visitor_management.repos;

import io.bootify.l11_visitor_management.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleNameIgnoreCase(String roleName);

}
