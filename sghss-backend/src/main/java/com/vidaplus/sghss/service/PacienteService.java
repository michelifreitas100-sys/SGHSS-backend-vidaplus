package com.vidaplus.sghss.service;

import com.vidaplus.sghss.audit.AuditService;
import com.vidaplus.sghss.dto.PacienteDTO;
import com.vidaplus.sghss.exception.BusinessException;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.model.Paciente;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.security.AesEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final AesEncryptor aesEncryptor;
    private final AuditService auditService;

    @Transactional
    public PacienteDTO.Response criar(PacienteDTO.Request request) {
        String cpfCriptografado = aesEncryptor.encrypt(request.cpf());

        if (pacienteRepository.existsByCpf(cpfCriptografado)) {
            throw new BusinessException("Ja existe um paciente cadastrado com este CPF");
        }

        Paciente paciente = Paciente.builder()
                .nome(request.nome())
                .cpf(cpfCriptografado)
                .dataNascimento(request.dataNascimento())
                .telefone(request.telefone())
                .endereco(request.endereco())
                .historicoClinico(request.historicoClinico())
                .build();

        paciente = pacienteRepository.save(paciente);
        auditService.registrar("CREATE", "PACIENTE", paciente.getId(), "Paciente cadastrado: " + paciente.getNome());

        return toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteDTO.Response> listarTodos() {
        return pacienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PacienteDTO.Response buscarPorId(Long id) {
        Paciente paciente = buscarEntidade(id);
        auditService.registrar("ACCESS", "PACIENTE", id, "Consulta de dados do paciente");
        return toResponse(paciente);
    }

    @Transactional
    public PacienteDTO.Response atualizar(Long id, PacienteDTO.Request request) {
        Paciente paciente = buscarEntidade(id);

        paciente.setNome(request.nome());
        paciente.setCpf(aesEncryptor.encrypt(request.cpf()));
        paciente.setDataNascimento(request.dataNascimento());
        paciente.setTelefone(request.telefone());
        paciente.setEndereco(request.endereco());
        paciente.setHistoricoClinico(request.historicoClinico());

        paciente = pacienteRepository.save(paciente);
        auditService.registrar("UPDATE", "PACIENTE", paciente.getId(), "Dados do paciente atualizados");

        return toResponse(paciente);
    }

    @Transactional
    public void deletar(Long id) {
        Paciente paciente = buscarEntidade(id);
        pacienteRepository.delete(paciente);
        auditService.registrar("DELETE", "PACIENTE", id, "Paciente removido: " + paciente.getNome());
    }

    private Paciente buscarEntidade(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente nao encontrado com id: " + id));
    }

    private PacienteDTO.Response toResponse(Paciente p) {
        return new PacienteDTO.Response(
                p.getId(),
                p.getNome(),
                aesEncryptor.decrypt(p.getCpf()),
                p.getDataNascimento(),
                p.getTelefone(),
                p.getEndereco(),
                p.getHistoricoClinico(),
                p.getCriadoEm(),
                p.getAtualizadoEm()
        );
    }
}
