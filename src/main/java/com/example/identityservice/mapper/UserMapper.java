package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.UserRequest;
import com.example.identityservice.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserRequest request);
}
