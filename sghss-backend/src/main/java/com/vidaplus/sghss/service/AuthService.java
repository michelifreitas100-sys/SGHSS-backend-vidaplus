package com.vidaplus.sghss.service;

import com.vidaplus.sghss.audit.AuditService;
import com.vidaplus.sghss.dto.LoginRequest;
import com.vidaplus.sghss.dto.LoginResponse;
import com.vidaplus.sghss.dto.RegisterRequest;
import com.vidaplus.sghss.exception.BusinessException;
import com.vidaplus.sghss.model.Usuario;
import com.vidaplus.sghss.repository.UsuarioRepository;
import com.vidaplus.sghss.security.JwtUtil;
import com.vidaplus.sghss.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

    public void registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(request.role())
                .build();

        usuarioRepository.save(usuario);
        auditService.registrar("CREATE", "USUARIO", usuario.getId(),
                "Cadastro de novo usuario com papel " + usuario.getRole());
    }

    public LoginResponse login(LoginRequest request) {
        // Lanca BadCredentialsException automaticamente se invalido.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtUtil.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(request.email()).orElseThrow();
        auditService.registrar("LOGIN", "USUARIO", usuario.getId(), "Login realizado com sucesso");

        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return LoginResponse.of(token, request.email(), role);
    }
}