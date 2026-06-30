package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.PacienteDTO;
import com.vidaplus.sghss.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pacientes", description = "Cadastro e gestao de pacientes do SGHSS")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL')")
    @Operation(summary = "Cadastrar paciente")
    public ResponseEntity<PacienteDTO.Response> criar(@Valid @RequestBody PacienteDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL')")
    @Operation(summary = "Listar todos os pacientes")
    public ResponseEntity<List<PacienteDTO.Response>> listar() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL', 'PACIENTE')")
    @Operation(summary = "Buscar paciente por ID")
    public ResponseEntity<PacienteDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL')")
    @Operation(summary = "Atualizar dados do paciente")
    public ResponseEntity<PacienteDTO.Response> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody PacienteDTO.Request request) {
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover paciente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
