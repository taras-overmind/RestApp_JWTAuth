package com.taras_overmind.repository;

import com.taras_overmind.model.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
        List<ContactEntity> findByUserId(Long user_id);
}
