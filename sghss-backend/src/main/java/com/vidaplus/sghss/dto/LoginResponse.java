package com.vidaplus.sghss.dto;

public record LoginResponse(
        String token,
        String tipo,
        String email,
        String role
) {
    public static LoginResponse of(String token, String email, String role) {
        return new LoginResponse(token, "Bearer", email, role);
    }
}
