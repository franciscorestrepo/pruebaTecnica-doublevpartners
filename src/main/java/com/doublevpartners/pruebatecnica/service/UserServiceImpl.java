package com.doublevpartners.pruebatecnica.service;

import com.doublevpartners.pruebatecnica.dto.UserDTO;
import com.doublevpartners.pruebatecnica.exception.ResourceNotFoundException;
import com.doublevpartners.pruebatecnica.model.User;
import com.doublevpartners.pruebatecnica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setNombres(userDTO.getNombres());
        user.setApellidos(userDTO.getApellidos());
        user.setFechaCreacion(LocalDateTime.now());
        user.setFechaActualizacion(LocalDateTime.now());

        userRepository.save(user);
        return entityToDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado"));

        user.setNombres(userDTO.getNombres());
        user.setApellidos(userDTO.getApellidos());
        user.setFechaActualizacion(LocalDateTime.now());
        userRepository.save(user);
        return entityToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
       .orElseThrow(() ->
               new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado"));
        return entityToDTO(user);
    }

    private UserDTO entityToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getFechaCreacion(),
                user.getFechaActualizacion()
        );
    }
}
