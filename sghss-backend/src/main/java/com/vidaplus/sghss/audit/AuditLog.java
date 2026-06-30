package com.vidaplus.sghss.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Registro de auditoria: quem fez o que, quando e em qual recurso.
 */
@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String usuarioResponsavel;

    @Column(nullable = false, length = 30)
    private String acao;
    
    @Column(nullable = false, length = 50)
    private String entidade;

    private Long entidadeId;

    @Column(length = 500)
    private String detalhes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @PrePersist
    public void prePersist() {
        this.dataHora = LocalDateTime.now();
    }
}
