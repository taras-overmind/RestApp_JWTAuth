package com.taras_overmind.services;

import com.taras_overmind.jwt.JwtUtils;
import com.taras_overmind.model.ERole;
import com.taras_overmind.model.Role;
import com.taras_overmind.model.User;
import com.taras_overmind.model.payload.SignupRequest;
import com.taras_overmind.repository.RoleRepository;
import com.taras_overmind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    public Optional<User> getUserByAuthorisationHeader(String authorizationHeader){
        String jwt = authorizationHeader.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createNewUser(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        return user;
    }
    public User save(User user){
        return userRepository.save(user);
    }

}
