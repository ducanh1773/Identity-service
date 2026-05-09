package com.example.identityservice.controller;

import com.example.identityservice.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.example.identityservice.dto.request.AuthenticationRequest.IntrospectRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.AuthenticationResponse.AuthenticationResponse;
import com.example.identityservice.dto.response.AuthenticationResponse.IntrospectResponse;
import com.example.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return authenticationService.login(authenticationRequest);
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introsp(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var introspect = authenticationService.introspect(introspectRequest);
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.success(authenticationService.introspect(introspectRequest));
        return apiResponse;

    }

}
