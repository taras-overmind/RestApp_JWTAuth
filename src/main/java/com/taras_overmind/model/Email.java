package com.taras_overmind.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "emails")
@Data
@NoArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailAddress;

    public Email(String emailAddress) {
        this.emailAddress = emailAddress;
    }
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "contact_id")
//    @OnDelete(action = OnDeleteAction.SET_DEFAULT) // Use CASCADE for ON DELETE
//    private ContactEntity contact;


}
