package com.vidaplus.sghss.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PacienteDTO {

    /** Dados recebidos para criar/atualizar um paciente. */
    public record Request(
            @NotBlank(message = "O nome e obrigatorio") String nome,

            @NotBlank(message = "O CPF e obrigatorio")
            @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 digitos numericos")
            String cpf,

            LocalDate dataNascimento,
            String telefone,
            String endereco,
            String historicoClinico
    ) {
    }

    public record Response(
            Long id,
            String nome,
            String cpf,
            LocalDate dataNascimento,
            String telefone,
            String endereco,
            String historicoClinico,
            LocalDateTime criadoEm,
            LocalDateTime atualizadoEm
    ) {
    }
}
