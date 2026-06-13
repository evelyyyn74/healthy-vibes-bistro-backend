package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
    List<Recompensa> findByActivaTrue();
}