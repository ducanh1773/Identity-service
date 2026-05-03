package com.example.identityservice.dto.request;

import jakarta.persistence.Access;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String userName;
    @Size(min = 8, message = "Password must be at least 8 character")
    private String password;
    @Size(min = 8, message = "Password must be at least 8 character")
    private String firstName;
    private String lastName;
}
