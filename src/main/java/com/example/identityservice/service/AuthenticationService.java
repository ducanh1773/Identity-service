package com.example.identityservice.service;

import com.example.identityservice.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.example.identityservice.dto.request.AuthenticationRequest.IntrospectRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.AuthenticationResponse.AuthenticationResponse;
import com.example.identityservice.dto.response.AuthenticationResponse.IntrospectResponse;
import com.example.identityservice.entity.Users;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.repository.UserRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static com.example.identityservice.exception.ErrorCode.PASSWORD_NOT_CORRECT;
import static com.example.identityservice.exception.ErrorCode.USER_NOT_EXISTED;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    private static final String SIGNER_KEY =
            "1234567890123456789012345678901234567890123456789012345678901234";

    public ApiResponse<AuthenticationResponse> login(AuthenticationRequest authenticationRequest) {
        Optional<Users> usersOptional = userRepository.findByuserName(authenticationRequest.getUsername());
        if (usersOptional == null || usersOptional.isEmpty()) {
            throw new AppException(USER_NOT_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(authenticationRequest.getPassword(), usersOptional.get().getPassword())) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setAuthenticated(true);
            authenticationResponse.setJwt(generateToken(authenticationRequest.getUsername()));
            ApiResponse apiResponse = new ApiResponse<>();
            return apiResponse.success(authenticationResponse);
        } else {
            throw new AppException(PASSWORD_NOT_CORRECT);
        }


    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token = introspectRequest.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verify && expityTime.after(new Date()))
                .build();


    }

    private String generateToken(String username) {

        try {
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("indentity-service")
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                    ))
                    .claim("userId", "Custom")
                    .build();


            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

            JWSObject jwsObject = new JWSObject(jwsHeader, payload);

            jwsObject.sign(new MACSigner(SIGNER_KEY));

            return jwsObject.serialize();
            // Serialize token string
        } catch (Exception ex) {
            System.out.println("Exception : " + ex);
        }

        return null;
    }

}
