package bt.edu.gcit.usermicroservice.security;

import java.security.Security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ShopmeSecurityConfig {
    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/users/{id}/enabled").permitAll()
                .requestMatchers("/api/roles/**").permitAll()
                );

        http.csrf().disable();
        return http.build();
    }
}
