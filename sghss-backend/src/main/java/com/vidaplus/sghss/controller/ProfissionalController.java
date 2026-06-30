package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.ProfissionalDTO;
import com.vidaplus.sghss.service.ProfissionalService;
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
@RequestMapping("/profissionais")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Profissionais", description = "Cadastro e gestao de profissionais de saude do SGHSS")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar profissional de saude")
    public ResponseEntity<ProfissionalDTO.Response> criar(@Valid @RequestBody ProfissionalDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL')")
    @Operation(summary = "Listar todos os profissionais")
    public ResponseEntity<List<ProfissionalDTO.Response>> listar() {
        return ResponseEntity.ok(profissionalService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL')")
    @Operation(summary = "Buscar profissional por ID")
    public ResponseEntity<ProfissionalDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar dados do profissional")
    public ResponseEntity<ProfissionalDTO.Response> atualizar(@PathVariable Long id,
                                                                 @Valid @RequestBody ProfissionalDTO.Request request) {
        return ResponseEntity.ok(profissionalService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover profissional")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        profissionalService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
