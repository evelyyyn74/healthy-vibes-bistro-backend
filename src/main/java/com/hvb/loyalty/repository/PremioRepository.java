// PremioRepository.java
package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PremioRepository extends JpaRepository<Premio, Long> {
    @Query("SELECT MIN(p.puntosRequeridos) FROM Premio p WHERE p.disponible = true")
    Optional<Integer> findMinPuntosRequeridos();
}