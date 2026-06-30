package com.vidaplus.sghss.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class ProfissionalDTO {

    public record Request(
            @NotBlank(message = "O nome e obrigatorio") String nome,

            @NotBlank(message = "O CPF e obrigatorio")
            @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 digitos numericos")
            String cpf,

            @NotBlank(message = "O registro profissional e obrigatorio (ex.: CRM, COREN)")
            String registroProfissional,

            @NotBlank(message = "A especialidade e obrigatoria")
            String especialidade,

            String telefone
    ) {
    }

    public record Response(
            Long id,
            String nome,
            String cpf,
            String registroProfissional,
            String especialidade,
            String telefone,
            LocalDateTime criadoEm,
            LocalDateTime atualizadoEm
    ) {
    }
}
