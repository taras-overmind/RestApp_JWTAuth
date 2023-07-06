package com.taras_overmind.services;

import com.taras_overmind.model.*;
import com.taras_overmind.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    UserService userService;

    public List<ContactEntity> getUserContacts(User user){
        return contactRepository.findByUserId(user.getId());
    }

    public Optional<ContactEntity> getContactByNameAndUser(String contactName, User user){
        return contactRepository.findByNameContactAndUser(contactName, user);
    }
    public List<ContactDTO> mapEntitiesToDTO(List<ContactEntity> contacts){
        List<ContactDTO> resultList = new ArrayList<>();
        for (var contact : contacts) {
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.setName(contact.getNameContact());
            contactDTO.setPhoneNumbers(new HashSet<>());
            contactDTO.setEmails(new HashSet<>());
            contactDTO.getPhoneNumbers().addAll(contact.getPhoneNumbers().stream().map(PhoneNumber::getNumber)
                    .collect(Collectors.toSet()));
            contactDTO.getEmails().addAll(contact.getEmails().stream().map(Email::getEmailAddress)
                    .collect(Collectors.toSet()));
            resultList.add(contactDTO);
        }
        return resultList;
    }
    public ContactEntity mapDtoToEntity(ContactDTO contactDTO, User user){
        ContactEntity contact = new ContactEntity();
        contact.setNameContact(contactDTO.getName());
        contact.setUser(user);

        contact.getPhoneNumbers().addAll(contactDTO.getPhoneNumbers().stream().map(PhoneNumber::new)
                .collect(Collectors.toSet()));
        contact.getEmails().addAll(contactDTO.getEmails().stream().map(Email::new)
                .collect(Collectors.toSet()));
        return contact;
    }
    public String createCheck(ContactDTO contact, User user){
        if(contactRepository.findByNameContactAndUser(contact.getName(), user).isPresent())
            return "You already have a contact with name:"+contact.getName();
        emailAndNumberFilter(contact);
        return null;

    }
    public String editCheck(ContactDTO contact, User user){
        if(contactRepository.findByNameContactAndUser(contact.getName(), user).isEmpty())
            return "No contact with name: "+contact.getName();
        emailAndNumberFilter(contact);
       return null;
    }
    public void copyEmailsAndNumbers(ContactEntity contact, ContactDTO contactDTO){
        contact.getEmails().clear();
        contact.getPhoneNumbers().clear();

        contact.getPhoneNumbers().addAll(contactDTO.getPhoneNumbers().stream().map(PhoneNumber::new)
                .collect(Collectors.toSet()));
        contact.getEmails().addAll(contactDTO.getEmails().stream().map(Email::new)
                .collect(Collectors.toSet()));
    }
    private void emailAndNumberFilter(ContactDTO contact){
        String phoneNumberPattern = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        contact.setPhoneNumbers(contact.getPhoneNumbers().stream()
                .filter(str-> Pattern.matches(phoneNumberPattern, str)).collect(Collectors.toSet()));
        contact.setEmails(contact.getEmails().stream()
                .filter(str->Pattern.matches(emailPattern, str)).collect(Collectors.toSet()));
    }
    public void delete(ContactEntity contact){
        contact.getEmails().clear();
        contact.getPhoneNumbers().clear();
        contactRepository.delete(contact);
    }

    public void save(ContactEntity contact){
        contactRepository.save(contact);
    }
}
