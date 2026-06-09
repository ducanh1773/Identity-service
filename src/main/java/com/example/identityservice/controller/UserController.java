package com.example.identityservice.controller;

import com.example.identityservice.dto.request.UserRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.Users;
import com.example.identityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody UserRequest userRequest) {

        return userService.create(userRequest);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        return userService.update(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteById(@PathVariable Integer id) {
        return userService.deleteById(id);
    }

    @PostMapping("/create-and-update")
    public ApiResponse<UserResponse> createAndUpdate(@RequestBody UserRequest userRequest) {
        return userService.createAndUpdate(userRequest);
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAll() {
        return userService.getAll();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getById() {
        return userService.myInfo();
    }

}
