package com.github.yuriiyukh.stogram.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.repo.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserEntity user = userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User " + userName + " not found"));
        return build(user);
    }

    public UserEntity loadUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("No user witn id " + id));
    }

    public static UserEntity build(UserEntity user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        
        return new UserEntity(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getPassword(), 
                authorities
                );
    }

}
