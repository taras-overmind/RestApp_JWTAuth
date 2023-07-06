package com.taras_overmind.repository;

import com.taras_overmind.model.ContactEntity;
import com.taras_overmind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
        List<ContactEntity> findByUserId(Long user_id);
        Optional<ContactEntity> findByNameContactAndUser(String contactName, User user);
}
