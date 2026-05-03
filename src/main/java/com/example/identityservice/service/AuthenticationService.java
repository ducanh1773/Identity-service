package com.example.identityservice.service;

import com.example.identityservice.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.AuthenticationResponse.AuthenticationResponse;
import com.example.identityservice.entity.Users;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.identityservice.exception.ErrorCode.PASSWORD_NOT_CORRECT;
import static com.example.identityservice.exception.ErrorCode.USER_NOT_EXISTED;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    public ApiResponse<AuthenticationResponse> login(AuthenticationRequest authenticationRequest){
        Optional<Users>  usersOptional = userRepository.findByuserName(authenticationRequest.getUsername());
        if(usersOptional == null || usersOptional.isEmpty()){
            throw new AppException(USER_NOT_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(passwordEncoder.matches(authenticationRequest.getPassword(), usersOptional.get().getPassword())){
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setFirstName(usersOptional.get().getFirstName());
            authenticationResponse.setLastName(usersOptional.get().getLastName());
            authenticationResponse.setUserName(usersOptional.get().getUserName());
            authenticationResponse.setPassword(usersOptional.get().getPassword());
            ApiResponse apiResponse = new ApiResponse<>();
            return apiResponse.success(authenticationResponse);
        }else{
            throw new AppException(PASSWORD_NOT_CORRECT);
        }


    }

}
