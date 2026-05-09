package com.example.identityservice.dto.response.AuthenticationResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private boolean authenticated;
    private String jwt;
}
