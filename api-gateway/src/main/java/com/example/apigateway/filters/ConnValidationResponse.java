package com.example.apigateway.filters;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class ConnValidationResponse {
    private String status;
    private boolean isAuthenticated;
    private String methodType;
    private String username;
    private List<Authorities> authorities;
}