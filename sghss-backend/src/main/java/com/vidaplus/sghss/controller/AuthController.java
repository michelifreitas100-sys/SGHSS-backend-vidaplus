package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.LoginRequest;
import com.vidaplus.sghss.dto.LoginResponse;
import com.vidaplus.sghss.dto.RegisterRequest;
import com.vidaplus.sghss.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacao", description = "Cadastro (sign-up) e login de usuarios do SGHSS")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Cadastrar novo usuario", description = "Cria um usuario com papel ADMIN, PROFISSIONAL ou PACIENTE")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RegisterRequest request) {
        authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Retorna um token JWT valido para uso nos demais endpoints")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
