package com.taras_overmind.controller;

import com.taras_overmind.jwt.JwtUtils;
import com.taras_overmind.model.*;
import com.taras_overmind.repository.ContactRepository;
import com.taras_overmind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contacts")
public class SimpleController {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping()
    public List<ContactDTO> getContacts(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long user_id = userRepository.findByUsername(username).get().getId();
        var contacts = contactRepository.findByUserId(user_id);
        List<ContactDTO> responseList = new ArrayList<>();
        for (var contact : contacts) {
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.setName(contact.getNameContact());
            contactDTO.setPhoneNumbers(new HashSet<>());
            contactDTO.setEmails(new HashSet<>());
            contactDTO.getPhoneNumbers().addAll(contact.getPhoneNumbers().stream().map(PhoneNumber::getNumber)
                    .collect(Collectors.toSet()));
            contactDTO.getEmails().addAll(contact.getEmails().stream().map(Email::getEmailAddress)
                    .collect(Collectors.toSet()));
            responseList.add(contactDTO);
        }
        return responseList;
    }

    @PostMapping
    public void addContact(@RequestBody ContactDTO contactDTO, @RequestHeader("Authorization") String authorizationHeader){
        String jwt = authorizationHeader.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).get();

        ContactEntity contact = new ContactEntity();
        contact.setNameContact(contactDTO.getName());
        contact.setUser(user);

        contact.getPhoneNumbers().addAll(contactDTO.getPhoneNumbers().stream().map(PhoneNumber::new)
                .collect(Collectors.toSet()));
        contact.getEmails().addAll(contactDTO.getEmails().stream().map(Email::new)
                .collect(Collectors.toSet()));


        contactRepository.save(contact);
    }

    @DeleteMapping()
    public void deleteContact(){}


}
