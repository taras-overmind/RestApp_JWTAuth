package com.taras_overmind.services;

import com.taras_overmind.jwt.JwtUtils;
import com.taras_overmind.model.ERole;
import com.taras_overmind.model.Role;
import com.taras_overmind.model.UserDetailsImpl;
import com.taras_overmind.model.payload.SignupRequest;
import com.taras_overmind.repository.RoleRepository;
import com.taras_overmind.repository.UserRepository;
import com.taras_overmind.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }



}