package com.example.identityservice.dto.response;

import com.example.identityservice.dto.response.AuthenticationResponse.IntrospectResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result;

    public ApiResponse(IntrospectResponse introspect) {
    }

    public ApiResponse success(T object){
        ApiResponse apiResponse = new ApiResponse(200 , "Success" , object);
        return apiResponse;
    }
}
