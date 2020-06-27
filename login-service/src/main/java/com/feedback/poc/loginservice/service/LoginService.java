package com.feedback.poc.loginservice.service;

import com.feedback.poc.loginservice.repository.LoginDao;
import com.feedback.poc.loginservice.domain.Employee;
import com.feedback.poc.loginservice.domain.LoginResponse;
import com.feedback.poc.loginservice.domain.UpdateResponse;
import com.feedback.poc.loginservice.domain.UserCredential;
import com.feedback.poc.loginservice.domain.RegisterResponse;
import com.feedback.poc.loginservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(value = "loginService")
public class LoginService {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private JwtUtil jwtUtil;

    public RegisterResponse register(Employee employee) {
        return loginDao.register(employee);

    }

    /**
     *
     * @param userCredential
     * @return
     */
    public LoginResponse validateLoginCredentials(UserCredential userCredential) {
        Boolean validResponse =  loginDao.login(userCredential);
        LoginResponse loginResponse = new LoginResponse();
        if(validResponse){
            appUserDetailsService.setUserCredentials(userCredential);
            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(userCredential.getUsername());
            final String jwtToken = jwtUtil.generateToken(userDetails);
            loginResponse.setJwt(jwtToken);
            loginResponse.setStatus("success");
            loginResponse.setUserName(userCredential.getUsername());
        }
        else
            loginResponse.setStatus("failure");

        return loginResponse;
    }


    public String remove(UserCredential userCredential) {
        return loginDao.removeUser(userCredential);
    }

    /**
     *
     * @param employee
     * @return
     */
    public UpdateResponse update(Employee employee) {
        return loginDao.updateUser(employee);
    }
}
