package com.feedback.poc.loginservice.controller;

import com.feedback.poc.loginservice.domain.*;
import com.feedback.poc.loginservice.service.LoginService;
//import com.netflix.appinfo.InstanceInfo;
//import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.ws.Response;


@RestController
public class LoginServiceRestController {
    Logger logger= LoggerFactory.getLogger(LoginServiceRestController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody @Valid Employee employee){
        logger.info("Employee Details recieved {}", employee);
        RegisterResponse registerResponse = loginService.register(employee);
        if(registerResponse.getStatus()!=-1)
            return ResponseEntity.ok().body(registerResponse);
        else
            return ResponseEntity.badRequest().body(registerResponse);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody UserCredential userCredential) {
        logger.info("Trying to login {}", userCredential.getUsername());
        LoginResponse loginResponse = loginService.validateLoginCredentials(userCredential);
        if(loginResponse.getStatus().equalsIgnoreCase("success"))
            return ResponseEntity.ok().body(loginResponse);
        else
            return ResponseEntity.badRequest().body(loginResponse);
    }

    @DeleteMapping(value = "/remove")
    public ResponseEntity<?> removeUser(@RequestBody UserCredential userCredential, HttpServletResponse response){
        logger.info("Request to remove user {}", userCredential.getUsername());
        String result =  loginService.remove(userCredential);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestBody Employee employee, HttpServletResponse response){
        logger.info("Request to update user {}", employee.getEmployeeId());
        UpdateResponse updateResponse = loginService.update(employee);
        if(updateResponse.getUpdateStatus().equalsIgnoreCase("updated")){
            return ResponseEntity.ok().body(updateResponse);
        }
        else
            return ResponseEntity.badRequest().body(updateResponse);
    }

//    @Autowired
//    private EurekaClient eurekaClient;
//
    @Bean
    public RestTemplate RestTemplate() {
        return new RestTemplate();
    }
//
//    @GetMapping("/healthCheck")
//    public String method2() {
//        return "UP";
//    }
//    //TODO: We can use FeignClient here
//    @GetMapping("/healthCheckApi")
//    public String method() {
//
//        InstanceInfo instance = eurekaClient.getNextServerFromEureka("LOGIN-SERVICE", false);
//        String response = restTemplate.getForObject(instance.getHomePageUrl() + "/healthCheck", String.class);
//        return "Instance called is : " + instance.getHomePageUrl() + " <br/><br/> And Response : " + response;
//    }

    @Autowired
    private RestTemplate restTemplate;
}
