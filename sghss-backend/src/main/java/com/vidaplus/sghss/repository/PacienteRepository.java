package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCpf(String cpfCriptografado);
    boolean existsByCpf(String cpfCriptografado);
}
