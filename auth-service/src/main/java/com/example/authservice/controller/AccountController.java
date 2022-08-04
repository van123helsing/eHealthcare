package com.example.authservice.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final JdbcDaoImpl service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> get(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) request.getAttribute("authorities");
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
                .username(username).authorities(grantedAuthorities)
                .isAuthenticated(true).build());
    }

    @Getter
    @Builder
    @ToString
    public static class ConnValidationResponse {
        private String status;
        private boolean isAuthenticated;
        private String methodType;
        private String username;
        private List<GrantedAuthority> authorities;
    }
}
