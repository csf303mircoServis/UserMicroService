package bt.edu.gcit.usermicroservice.security;

import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2UserService;
import bt.edu.gcit.usermicroservice.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class ShopmeSecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("Admin")
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").hasAuthority("Admin")
                .requestMatchers("/api/roles/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/countries").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/countries").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/countries").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/states/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/states/{country_id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/states").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/states").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/customer/*").permitAll()
                .requestMatchers("/login/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated()) // Protect everything else
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler);

        http.csrf().disable();

        return http.build();
    }

}
