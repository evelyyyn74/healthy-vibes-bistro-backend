package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Canje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CanjeRepository extends JpaRepository<Canje, Long> {
    List<Canje> findByClienteId(Long clienteId);
}