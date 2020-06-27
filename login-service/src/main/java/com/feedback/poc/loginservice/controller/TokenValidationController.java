package com.feedback.poc.loginservice.controller;

import com.feedback.poc.loginservice.service.TokenValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController("/token/validate")
public class TokenValidationController {

    @Autowired
    private TokenValidationService tokenValidationService;

    @GetMapping
    public String validateToken(HttpServletRequest request){
      log.info("Validating Token");
      return tokenValidationService.validateToken(request.getHeader("Authorization"));
    }

}
