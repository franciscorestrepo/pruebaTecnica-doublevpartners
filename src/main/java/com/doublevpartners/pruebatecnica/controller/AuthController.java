package com.doublevpartners.pruebatecnica.controller;

import com.doublevpartners.pruebatecnica.dto.AuthRequest;
import com.doublevpartners.pruebatecnica.dto.AuthResponse;
import com.doublevpartners.pruebatecnica.security.JwtUtil;
import com.doublevpartners.pruebatecnica.security.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserDetailsService userDetailsService;
        private final JwtUtil jwtUtil;

        @Autowired
        public AuthController(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
                              AuthenticationManager authenticationManager) {
            this.jwtUtil = jwtUtil;
            this.userDetailsService = userDetailsService;
            this.authenticationManager = authenticationManager;
        }

        @Operation(summary = "Login para obtener el token JWT",
                description = "Este endpoint permite a los usuarios autenticarse y obtener un " +
                        "token JWT para acceder a los endpoints protegidos.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Login exitoso",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
                })


        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword());
            authenticationManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }



    }
