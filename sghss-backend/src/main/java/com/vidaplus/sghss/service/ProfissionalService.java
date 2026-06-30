package com.vidaplus.sghss.service;

import com.vidaplus.sghss.audit.AuditService;
import com.vidaplus.sghss.dto.ProfissionalDTO;
import com.vidaplus.sghss.exception.BusinessException;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.model.Profissional;
import com.vidaplus.sghss.repository.ProfissionalRepository;
import com.vidaplus.sghss.security.AesEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final AesEncryptor aesEncryptor;
    private final AuditService auditService;

    @Transactional
    public ProfissionalDTO.Response criar(ProfissionalDTO.Request request) {
        String cpfCriptografado = aesEncryptor.encrypt(request.cpf());

        if (profissionalRepository.existsByCpf(cpfCriptografado)) {
            throw new BusinessException("Ja existe um profissional cadastrado com este CPF");
        }
        if (profissionalRepository.existsByRegistroProfissional(request.registroProfissional())) {
            throw new BusinessException("Ja existe um profissional cadastrado com este registro profissional");
        }

        Profissional profissional = Profissional.builder()
                .nome(request.nome())
                .cpf(cpfCriptografado)
                .registroProfissional(request.registroProfissional())
                .especialidade(request.especialidade())
                .telefone(request.telefone())
                .build();

        profissional = profissionalRepository.save(profissional);
        auditService.registrar("CREATE", "PROFISSIONAL", profissional.getId(),
                "Profissional cadastrado: " + profissional.getNome());

        return toResponse(profissional);
    }

    @Transactional(readOnly = true)
    public List<ProfissionalDTO.Response> listarTodos() {
        return profissionalRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfissionalDTO.Response buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public ProfissionalDTO.Response atualizar(Long id, ProfissionalDTO.Request request) {
        Profissional profissional = buscarEntidade(id);

        profissional.setNome(request.nome());
        profissional.setCpf(aesEncryptor.encrypt(request.cpf()));
        profissional.setRegistroProfissional(request.registroProfissional());
        profissional.setEspecialidade(request.especialidade());
        profissional.setTelefone(request.telefone());

        profissional = profissionalRepository.save(profissional);
        auditService.registrar("UPDATE", "PROFISSIONAL", profissional.getId(), "Dados do profissional atualizados");

        return toResponse(profissional);
    }

    @Transactional
    public void deletar(Long id) {
        Profissional profissional = buscarEntidade(id);
        profissionalRepository.delete(profissional);
        auditService.registrar("DELETE", "PROFISSIONAL", id, "Profissional removido: " + profissional.getNome());
    }

    private Profissional buscarEntidade(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional nao encontrado com id: " + id));
    }

    private ProfissionalDTO.Response toResponse(Profissional p) {
        return new ProfissionalDTO.Response(
                p.getId(),
                p.getNome(),
                aesEncryptor.decrypt(p.getCpf()),
                p.getRegistroProfissional(),
                p.getEspecialidade(),
                p.getTelefone(),
                p.getCriadoEm(),
                p.getAtualizadoEm()
        );
    }
}
