package com.example.identityservice.service;

import com.example.identityservice.dto.request.UserRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.Users;
import com.example.identityservice.enums.Roles;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.exception.ErrorCode;
import com.example.identityservice.mapper.UserMapper;
import com.example.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.management.relation.Role;
import java.util.*;

import static com.example.identityservice.exception.ErrorCode.INVALID_PASSWORD;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;


    public ApiResponse<UserResponse> createAndUpdate(UserRequest userRequest) {
        Users result = new Users();
        if (userRequest.getId() != null) {
            Optional<Users> usersOptional = userRepository.findById(userRequest.getId());
            if (usersOptional == null || usersOptional.isEmpty()) {
                throw new RuntimeException("Cannot find user");
            }
            Users users = usersOptional.get();
            users.setUserName(userRequest.getUserName());
            users.setPassword(userRequest.getPassword());
            users.setFirstName(userRequest.getFirstName());
            users.setLastName(userRequest.getLastName());
            result = users;
            userRepository.save(users);
        } else {
            boolean checkUserName = userRepository.existsByuserName(userRequest.getUserName());
            if (checkUserName) {
                throw new AppException(ErrorCode.USER_EXISTED);
            }
            if (userRequest.getPassword().length() < 8) {
                throw new AppException(ErrorCode.INVALID_PASSWORD);
            }
            Users users = userMapper.toUser(userRequest);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            users.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            HashSet<String> roles = new HashSet<>();
            roles.add(Roles.USER.name());
//            users.setRole(roles);
            result = users;
            userRepository.save(users);
        }
        return new ApiResponse(200, "Ok", entityToResponse(result));
    }


    public ApiResponse<UserResponse> create(UserRequest userRequest) {

        boolean checkUserName = userRepository.existsByuserName(userRequest.getUserName());
        if (checkUserName) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRequest.getPassword().length() < 8) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        Users users = userMapper.toUser(userRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        users.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.save(users);
        return new ApiResponse(200, "Ok", entityToResponse(users));
    }

    @PostAuthorize("returnObject.result.username == authentication.name")
    public ApiResponse<UserResponse> getById(Integer id) {
        Optional<Users> users = userRepository.findById(id);
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Cannot find user");
        }
        ApiResponse apiResponse = new ApiResponse<>();
        return apiResponse.success(users.get());
    }

    public ResponseEntity<UserResponse> update(Integer id, UserRequest userRequest) {

        Optional<Users> usersOptional = userRepository.findById(id);
        if (usersOptional == null || usersOptional.isEmpty()) {
            throw new RuntimeException("Cannot find user");
        }
        Users users = usersOptional.get();
        users.setUserName(userRequest.getUserName());
        users.setPassword(userRequest.getPassword());
        users.setFirstName(userRequest.getFirstName());
        users.setLastName(userRequest.getLastName());
        userRepository.save(users);
        return ResponseEntity.ok(entityToResponse(users));
    }

    public ResponseEntity<UserResponse> deleteById(Integer id) {
        Optional<Users> users = userRepository.findById(id);
        userRepository.deleteById(id);
        return ResponseEntity.ok(entityToResponse(users.get()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getAll() {
        log.info("In method get users");
        List<Users> usersList = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (Users users : usersList) {
            UserResponse userResponse = entityToResponse(users);
            userResponseList.add(userResponse);
        }
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse.success(userResponseList);
    }

    public ApiResponse<UserResponse> myInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Users users = userRepository.findByuserName(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return new ApiResponse<>().success(this.entityToResponse(users));
    }


    private Users requestToEntity(UserRequest userRequest) {
        Users users = new Users();
        users.setUserName(userRequest.getUserName());
        users.setPassword(userRequest.getPassword());
        users.setFirstName(userRequest.getFirstName());
        users.setLastName(userRequest.getLastName());
        return users;
    }

    private UserResponse entityToResponse(Users users) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserName(users.getUserName());
        userResponse.setFirstName(users.getFirstName());
        userResponse.setLastName(users.getLastName());
//        userResponse.setRole(users.getRole());
        return userResponse;
    }


}
