package com.taras_overmind;

import com.taras_overmind.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityDemoApplicationTests {

	@Autowired
	ContactRepository contactRepository;
	@Test
	void contextLoads() {
		System.out.println(contactRepository.findById(13L).get());
	}

}
