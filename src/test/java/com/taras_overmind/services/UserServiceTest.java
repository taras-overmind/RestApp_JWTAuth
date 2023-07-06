package com.taras_overmind.services;

import com.taras_overmind.jwt.JwtUtils;
import com.taras_overmind.model.User;
import com.taras_overmind.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class UserServiceTest {
    @Autowired
    JwtUtils jwtUtils;

    @Test
    void getUserByAuthorisationHeader() {
    }
    @Test
    public void testGetUserByAuthorisationHeader() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);

        String authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        String username = "john.doe";

        User user = new User();
        user.setUsername(username);

        when(jwtUtilsMock.getUserNameFromJwtToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
                .thenReturn(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        UserService userService = new UserService();
        userService.setJwtUtils(jwtUtilsMock);
        userService.setUserRepository(userRepository);

        Optional<User> result = userService.getUserByAuthorisationHeader(authorizationHeader);
        assertEquals(jwtUtils.getUserNameFromJwtToken(
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXJhcyIsImlhdCI6MTY4ODY2NTQ0MiwiZXhwIjoxNjg4NzUxODQyfQ" +
                        ".9De7KqCgNqoMTTAH1e2XcTFJIxIGXmWwAA9vXdijp7M"), "taras");
        assertEquals(Optional.of(user), result);
        verify(jwtUtilsMock, times(1)).getUserNameFromJwtToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(jwtUtilsMock, userRepository);
    }


}