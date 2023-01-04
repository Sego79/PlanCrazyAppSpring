package com.plancrazyappfrontofficespring.service;

import com.plancrazyappfrontofficespring.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDetails user = appUserRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " not found"));

        return user;
    }

}
