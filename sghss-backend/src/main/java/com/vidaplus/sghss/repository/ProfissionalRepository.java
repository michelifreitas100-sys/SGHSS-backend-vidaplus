package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByCpf(String cpfCriptografado);
    boolean existsByCpf(String cpfCriptografado);
    boolean existsByRegistroProfissional(String registroProfissional);
}
