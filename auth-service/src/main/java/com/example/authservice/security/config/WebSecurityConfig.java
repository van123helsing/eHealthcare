package com.example.authservice.security.config;

import com.example.authservice.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.authservice.jwt.JwtConfig;
import com.example.authservice.jwt.JwtVerifier;
import com.example.authservice.security.AuthTokenFilter;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    @Autowired
    DataSource dataSource;
    @Autowired
    private EurekaClient eurekaClient;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Application application = eurekaClient.getApplication("API-GATEWAY");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter())
                .addFilterAfter(new JwtVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/**").hasIpAddress(instanceInfo.getIPAddr())
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(this.passwordEncoder)
                .dataSource(dataSource);
    }

    private AuthTokenFilter authenticationFilter() throws Exception {
        return new AuthTokenFilter(authenticationManager(), jwtConfig, secretKey);
    }
}
