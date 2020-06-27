package com.feedback.poc.loginservice.service;

import com.feedback.poc.loginservice.domain.UserCredential;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private UserCredential userCredential;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserBuilder builder = null;
        if(null != userCredential){
            builder = User.withUsername(userCredential.getUsername());
            builder.password(userCredential.getPassword().toString());
            builder.roles("ADMIN");
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }
        return builder.build();
    }

    public void setUserCredentials(UserCredential userCredential){
        this.userCredential = userCredential;
    }
}
