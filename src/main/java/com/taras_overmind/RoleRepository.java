package com.taras_overmind;
import java.util.Optional;

import com.taras_overmind.model.ERole;
import com.taras_overmind.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}