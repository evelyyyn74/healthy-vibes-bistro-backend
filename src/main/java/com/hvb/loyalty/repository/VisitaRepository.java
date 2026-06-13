package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitaRepository extends JpaRepository<Visita, Long> {
    List<Visita> findByClienteId(Long clienteId);
}