package com.doublevpartners.pruebatecnica.controller;

import com.doublevpartners.pruebatecnica.dto.UserDTO;
import com.doublevpartners.pruebatecnica.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Crear un nuevo usuario",
            description = "Este endpoint permite crear un nuevo usuario en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos")
            })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }


    @Operation(summary = "Actualizar un usuario existente",
            description = "Este endpoint permite actualizar la información de un usuario en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id,
                                              @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    @Operation(summary = "Obtener todos los usuarios",
            description = "Este endpoint permite obtener una lista de todos los usuarios registrados en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class)))            })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @Operation(summary = "Obtener usuario por ID",
            description = "Este endpoint permite obtener los detalles de un usuario utilizando su ID único.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
