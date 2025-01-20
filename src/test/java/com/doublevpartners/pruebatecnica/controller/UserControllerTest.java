package com.doublevpartners.pruebatecnica.controller;

import com.doublevpartners.pruebatecnica.dto.UserDTO;
import com.doublevpartners.pruebatecnica.security.JwtAuthFilter;
import com.doublevpartners.pruebatecnica.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnOkAndUserDTO() throws Exception {
        UserDTO requestDTO = new UserDTO(null, "Carlos", "López",
                LocalDateTime.of(2025, 1, 10, 10, 0),
                LocalDateTime.of(2025, 1, 10, 10, 0));

        UserDTO responseDTO = new UserDTO(UUID.randomUUID(), "Carlos", "López",
                LocalDateTime.of(2025, 1, 10, 10, 0),
                LocalDateTime.of(2025, 1, 10, 10, 0));

        BDDMockito.given(userService.createUser(any(UserDTO.class)))
                .willReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nombres").value("Carlos"));

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    void getAllUsers_ShouldReturnOkAndListOfUsers() throws Exception {
        UserDTO user1 = new UserDTO(
                UUID.randomUUID(), "Juan", "Pérez",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );
        UserDTO user2 = new UserDTO(
                UUID.randomUUID(), "Ana", "García",
                LocalDateTime.of(2025, 2, 2, 11, 0),
                LocalDateTime.of(2025, 2, 2, 11, 0)
        );
        List<UserDTO> userList = List.of(user1, user2);

        BDDMockito.given(userService.getAllUsers()).willReturn(userList);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombres").value("Juan"))
                .andExpect(jsonPath("$[1].nombres").value("Ana"));

        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_ShouldReturnOkAndEmptyList() throws Exception {
        BDDMockito.given(userService.getAllUsers()).willReturn(Collections.emptyList());
        mockMvc.perform(get("/api/users"))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnOkAndUserDTO() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO responseDTO = new UserDTO(
                userId, "Pepito", "Pérez",
                LocalDateTime.of(2025, 3, 10, 9, 0),
                LocalDateTime.of(2025, 3, 10, 9, 0)
        );
        BDDMockito.given(userService.getUserById(userId)).willReturn(responseDTO);
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.nombres").value("Pepito"));

        verify(userService).getUserById(userId);
    }
}

