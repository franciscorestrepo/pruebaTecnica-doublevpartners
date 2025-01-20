package com.doublevpartners.pruebatecnica.service;

import com.doublevpartners.pruebatecnica.dto.UserDTO;
import com.doublevpartners.pruebatecnica.exception.ResourceNotFoundException;
import com.doublevpartners.pruebatecnica.model.User;
import com.doublevpartners.pruebatecnica.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setNombres("Juan");
        user.setApellidos("Pérez");
        user.setFechaCreacion(LocalDateTime.of(2025, 1, 1, 10, 0));
        user.setFechaActualizacion(LocalDateTime.of(2025, 1, 1, 10, 0));

        userDTO = new UserDTO(
                null,
                "Juan",
                "Pérez",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );
    }

    @Test
    void createUser_ShouldSaveUserAndReturnDTO() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });
        UserDTO result = userService.createUser(userDTO);
        assertNotNull(result.getId());
        assertEquals("Juan", result.getNombres());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO updateDTO = new UserDTO(
                null,
                "Juan Updated",
                "Pérez Updated",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 2, 11, 0)
        );
        UserDTO result = userService.updateUser(userId, updateDTO);
        assertEquals("Juan Updated", result.getNombres());
        assertEquals("Pérez Updated", result.getApellidos());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                userService.updateUser(userId, userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);
        List<UserDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombres());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDTO result = userService.getUserById(userId);
        assertEquals("Juan", result.getNombres());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }
}
