package com.example.RedisForOrderManagementSystem.controller;

import com.example.RedisForOrderManagementSystem.AbstractIntegrationTest;
import com.example.SpringBootJWT.dto.CreateUserDto;
import com.example.SpringBootJWT.dto.LoginDto;
import com.example.SpringBootJWT.dto.LoginResponseDto;
import com.example.SpringBootJWT.dto.RegisterUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)

public class AuthControllerIntegrationTest
        extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should register User")
    void shouldRegisterUser() {
        RestClient restClient =
                RestClient.builder()
                        .baseUrl(baseUrl())
                        .build();

        CreateUserDto dto =
                new CreateUserDto();

        dto.setName("Aniket");
        dto.setEmail("register@test.com");
        dto.setPassword("password123");

        RegisterUserDto response =
                restClient.post()
                        .uri("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .retrieve()
                        .body(RegisterUserDto.class);

        assertNotNull(response);

        assertEquals(
                "Aniket",
                response.getName()
        );

    }

        @Test
        @DisplayName("Should reject duplicate email")
        void shoulRejectDuplicateEmail(){
            RestClient restClient = RestClient.builder()
                    .baseUrl(baseUrl())
                    .build();

            CreateUserDto dto = new CreateUserDto();
            //FIRST REGISTER
            dto.setName("Aniket");
            dto.setEmail("duplicate@test.com");
            dto.setPassword("password123");

            restClient.post()
                    .uri("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .body(RegisterUserDto.class);

            // SECOND REGISTER SHOULD FAIL

            assertThrows(
                    HttpClientErrorException.Conflict.class,
                    () -> restClient.post()
                            .uri("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(dto)
                            .retrieve()
                            .body(String.class)
            );

            int i = 9;
        }

        @Test
        @DisplayName("Should login successfully")
        void shouldLoginSuccessfully(){
            RestClient restClient =
                    RestClient.builder()
                            .baseUrl(baseUrl())
                            .build();

            CreateUserDto registerDto = new CreateUserDto();

            registerDto.setName("Aniket");
            registerDto.setEmail("aniket@test.com");
            registerDto.setPassword("password123");

            restClient.post()
                    .uri("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(registerDto)
                    .retrieve()
                    .body(RegisterUserDto.class);

            LoginDto loginDto = new LoginDto("aniket@test.com","password123");

            LoginResponseDto response =
                    restClient.post()
                            .uri("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(loginDto)
                            .retrieve()
                            .body(LoginResponseDto.class);

            assertNotNull(response);

            assertNotNull(response.getJwt());
        }

        @Test
        @DisplayName("Should access protected endpoint with JWT")
        void shouldAccessProtectedEndpoint(){
            RestClient restClient =
                    RestClient.builder()
                            .baseUrl(baseUrl())
                            .build();

            //REGISTER USER

            CreateUserDto registerDto = new CreateUserDto();

            registerDto.setName("User");
            registerDto.setEmail("user@test.com");
            registerDto.setPassword("password123");

            restClient.post()
                    .uri("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(registerDto)
                    .retrieve()
                    .body(RegisterUserDto.class);

            //LOGIN

            LoginDto loginDto =
                    new LoginDto("user@test.com","password123");

            LoginResponseDto loginResponse =
                    restClient.post()
                            .uri("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(loginDto)
                            .retrieve()
                            .body(LoginResponseDto.class);

            assertNotNull(loginResponse);

            String token =  loginResponse.getJwt();

            assertNotNull(token);

            // ACCESS PROTECTED ENDPOINT

            String response = restClient.get()
                    .uri("/api/v1/products")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer "+ token
                    )
                    .retrieve()
                    .body(String.class);

            assertNotNull(response);
        }




    private String baseUrl() {
        return "http://localhost:" + port;
    }
}
