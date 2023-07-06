package com.taras_overmind.services;

import com.taras_overmind.model.*;
import com.taras_overmind.repository.ContactRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.parser.Entity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContactServiceTest {
    @Autowired
    ContactService contactService;

    private List<ContactEntity> entityList;
    private List<ContactDTO> dtoList;
    ContactRepository contactRepository = Mockito.mock(ContactRepository.class);
    private User user;

    {
        entityList = new ArrayList<>();
        ContactEntity contact1 = new ContactEntity();
        contact1.setNameContact("Name1");
        contact1.setEmails(Set.of(new Email("aaa@gmail.com"), new Email("bbb@gmail.com")));
        contact1.setPhoneNumbers(Set.of(new PhoneNumber("+380687276812"), new PhoneNumber("+380687276813")));
        ContactEntity contact2 = new ContactEntity();
        contact2.setNameContact("Name2");
        contact2.setEmails(Set.of(new Email("ccc@gmail.com"), new Email("dddgmail.com")));
        contact2.setPhoneNumbers(Set.of(new PhoneNumber("a380687276812"), new PhoneNumber("+380687276813")));
        ContactEntity contact3 = new ContactEntity();
        contact3.setNameContact("Name3");
        contact3.setEmails(Set.of(new Email("eeee@gmail.com"), new Email("eee@gmail.com")));
        contact3.setPhoneNumbers(Set.of(new PhoneNumber("+380687276812"), new PhoneNumber("+380687276813")));
        entityList.add(contact1);
        entityList.add(contact2);
        entityList.add(contact3);

        dtoList = new ArrayList<>();
        ContactDTO contactDTO1 = new ContactDTO("Name1", Set.of("aaa@gmail.com", "bbb@gmail.com"),
                Set.of("+380687276812", "+380687276813"));
        ContactDTO contactDTO2 = new ContactDTO("Name2", Set.of("ccc@gmail.com", "dddgmail.com"),
                Set.of("+a380687276812", "+380687276813"));
        ContactDTO contactDTO3 = new ContactDTO("Name3", Set.of("eeee@gmail.com", "eee@gmail.com"),
                Set.of("+380687276812", "+380687276813"));
        dtoList.add(contactDTO1); dtoList.add(contactDTO2); dtoList.add(contactDTO3);
        User user = new User("username", "password");


    }

    @Test
    void mapEntitiesToDTO() {
        var resultList = contactService.mapEntitiesToDTO(entityList);
        assertEquals(resultList.size(), entityList.size());
        assertEquals(dtoList.get(0), resultList.get(0));
        assertEquals(dtoList.get(2), resultList.get(2));
    }

    @Test
    void mapDtoToEntity() {
        User user = new User("username", "password");
        var result = contactService.mapDtoToEntity(dtoList.get(0), user);
        assertEquals(result.getNameContact(), entityList.get(0).getNameContact());
        assertEquals(result.getPhoneNumbers(), entityList.get(0).getPhoneNumbers());
        assertEquals(result.getEmails(), entityList.get(0).getEmails());
    }


    @Test
    public void testCreateCheck_ContactDoesNotExist() {
        ContactRepository contactRepository = Mockito.mock(ContactRepository.class);
        User user = Mockito.mock(User.class);
        ContactDTO contact = new ContactDTO();
        contact.setName("John Doe");

        when(contactRepository.findByNameContactAndUser(contact.getName(), user))
                .thenReturn(Optional.empty());

        String result = contactService.createCheck(contact, null);

        assertNull(result);
    }

    @Test
    public void testCreateCheck_ContactExists() {
        ContactDTO contact = new ContactDTO();
        contact.setName("John Doe");

        when(contactRepository.findByNameContactAndUser(contact.getName(), user))
                .thenReturn(Optional.of(new ContactEntity()));
        ContactService contactService = new ContactService(contactRepository);
        String result = contactService.createCheck(contact, user);

        assertEquals("You already have a contact with name: John Doe", result);

        verify(contactRepository, times(1)).findByNameContactAndUser(contact.getName(), user);
        verifyNoMoreInteractions(contactRepository);
    }

    @Test
    public void testEditCheck_ContactDoesNotExist() {
        ContactDTO contact = new ContactDTO();
        contact.setName("John Doe");

        when(contactRepository.findByNameContactAndUser(contact.getName(), user))
                .thenReturn(Optional.empty());



        String result = contactService.editCheck(contact, user);

        assertEquals("No contact with name: John Doe", result);
    }
    @Test
    public void testEditCheck_ContactExists() {
        // Create a mock ContactDTO
        ContactDTO contact = new ContactDTO();
        contact.setName("John Doe");

        when(contactRepository.findByNameContactAndUser(contact.getName(), user))
                .thenReturn(Optional.of(new ContactEntity()));

        ContactService contactService = new ContactService(contactRepository);

        String result = contactService.editCheck(contact, user);

        assertNull(result);
    }


    @Test
    public void testFilterEmailsAndPhones_ValidInputs() {
        ContactDTO contact = new ContactDTO();
        contact.setName("John Doe");
        Set<String> emails = new HashSet<>();
        emails.add("john@example.com");
        emails.add("doe@example.com");
        contact.setEmails(emails);
        Set<String> phones = new HashSet<>();
        phones.add("+123456789");
        phones.add("0975223685");
        contact.setPhones(phones);

        contactService.filterEmailsAndPhones(contact);

        Set<String> expectedFilteredEmails = new HashSet<>();
        expectedFilteredEmails.add("john@example.com");
        expectedFilteredEmails.add("doe@example.com");
        assertEquals(expectedFilteredEmails, contact.getEmails());

        Set<String> expectedFilteredPhones = new HashSet<>();
        expectedFilteredPhones.add("0975223685");
        assertEquals(expectedFilteredPhones, contact.getPhones());
    }

    @Test
    public void testFilterEmailsAndPhones_InvalidInputs() {
        ContactDTO contact = new ContactDTO();
        contact.setName("John Doe");
        Set<String> emails = new HashSet<>();
        emails.add("john@example.com");
        emails.add("invalid-email");
        contact.setEmails(emails);
        Set<String> phones = new HashSet<>();
        phones.add("+380687276817");
        phones.add("invalid-phone");
        contact.setPhones(phones);

        contactService.filterEmailsAndPhones(contact);

        Set<String> expectedFilteredEmails = new HashSet<>();
        expectedFilteredEmails.add("john@example.com");
        assertEquals(expectedFilteredEmails, contact.getEmails());

        Set<String> expectedFilteredPhones = new HashSet<>();
        expectedFilteredPhones.add("+380687276817");
        assertEquals(expectedFilteredPhones, contact.getPhones());
    }
}