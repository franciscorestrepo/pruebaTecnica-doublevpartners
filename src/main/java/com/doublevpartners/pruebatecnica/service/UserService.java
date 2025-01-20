package com.doublevpartners.pruebatecnica.service;

import com.doublevpartners.pruebatecnica.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UUID userId, UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID userId);

}
