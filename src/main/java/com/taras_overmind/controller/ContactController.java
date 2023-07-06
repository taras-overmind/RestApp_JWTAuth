package com.taras_overmind.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taras_overmind.model.*;
import com.taras_overmind.repository.ContactRepository;
import com.taras_overmind.services.ContactService;
import com.taras_overmind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;
    @Autowired
    UserService userService;


    @GetMapping()
    public List<ContactDTO> getContacts(@RequestHeader("Authorization") String authorizationHeader) {

        User user = userService.getUserByAuthorisationHeader(authorizationHeader).get();
        List<ContactEntity> contacts = contactService.getUserContacts(user);

        return contactService.mapEntitiesToDTO(contacts);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addContact(@RequestBody ContactDTO contactDTO, @RequestHeader("Authorization") String authorizationHeader) {
        //TODO verification
        User user = userService.getUserByAuthorisationHeader(authorizationHeader).get();

        String check = contactService.createCheck(contactDTO, user);
        if (check != null)
            return check;

        ContactEntity contact = contactService.mapDtoToEntity(contactDTO, user);

        contactService.save(contact);

        return "Contact: " + contact.getNameContact() + " created successfully";
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.GONE)
    public String deleteContact(@RequestBody ContactDTO contactDTO, @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getUserByAuthorisationHeader(authorizationHeader).get();

        Optional<ContactEntity> contact = contactService.getContactByNameAndUser(contactDTO.getName(), user);
        if (contact.isEmpty())
            return "No contact with this name";

        contactService.delete(contact.get());
        return "Deleted successfully";
    }

    @PutMapping
    public String editContact(@RequestBody ContactDTO contactDTO, @RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getUserByAuthorisationHeader(authorizationHeader).get();

        String check = contactService.editCheck(contactDTO, user);
        if (check != null)
            return check;

        ContactEntity contact = contactService.getContactByNameAndUser(contactDTO.getName(), user).get();

        contactService.copyEmailsAndNumbers(contact, contactDTO);

        contactService.save(contact);
        return "Edited successfully";
    }

    @GetMapping
    @RequestMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@RequestHeader("Authorization") String authorizationHeader) throws IOException {

        var list = getContacts(authorizationHeader);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonContent = objectMapper.writeValueAsBytes(list);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "export.json");
        headers.setContentLength(jsonContent.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(jsonContent);

    }

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    public String importData(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        // Check if the file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Read the JSON file content
        byte[] jsonData = file.getBytes();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ContactDTO> list = objectMapper.readValue(jsonData, new TypeReference<>() {
        });
        for (var contact : list)
            addContact(contact, authorizationHeader);

        return "Data imported successfully";
    }


}
