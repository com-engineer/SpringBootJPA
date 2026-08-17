package com.example.SpringBootJWT.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        //without jwt
//        httpSecurity.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
//                                .requestMatchers("/api/v1/users/*/orders/**").hasAnyRole("ADMIN","USER")
//                                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
//                .anyRequest().authenticated());
////                .anyRequest().authenticated()).httpBasic(Customizer.withDefaults());//without using jwt
//        return httpSecurity.build();

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                This tells Spring:Do not create or use a server-side session to remember the logged-in user.
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**").permitAll()

                                //USER
                                .requestMatchers("/api/v1/users/me").hasRole("USER")

                                //ADMIN
                                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                                //ORDERS USER
                                .requestMatchers(HttpMethod.POST,"/api/v1/orders").hasRole("USER")
                                .requestMatchers("/api/v1/orders/my/**").hasRole("USER")

                                //ORDERS ADMIN
                                .requestMatchers(HttpMethod.GET,"/api/v1/orders").hasRole("ADMIN")
                                .requestMatchers("/api/v1/orders/user/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/orders/**").hasRole("ADMIN")

                                .anyRequest().authenticated())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
//    @Bean
////    without using jwt
//    UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder){
//        UserDetails admin = User.withUsername("Admin")
//                .roles("ADMIN")
//                .password(passwordEncoder.encode("pass123"))
//                .build();
//
//        UserDetails user = User.withUsername("Gaurav")
//                .roles("USER")
//                .password(passwordEncoder.encode("user123"))
//                .build();
//
//        return new InMemoryUserDetailsManager(user,admin);
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig){
        return authConfig.getAuthenticationManager();
    }
}
