package com.vidaplus.sghss.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void registrar(String acao, String entidade, Long entidadeId, String detalhes) {
        String usuario = obterUsuarioAtual();
        AuditLog log = AuditLog.builder()
                .usuarioResponsavel(usuario)
                .acao(acao)
                .entidade(entidade)
                .entidadeId(entidadeId)
                .detalhes(detalhes)
                .build();
        auditLogRepository.save(log);
    }

    private String obterUsuarioAtual() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            return auth.getName();
        }
        return "ANONIMO";
    }
}
