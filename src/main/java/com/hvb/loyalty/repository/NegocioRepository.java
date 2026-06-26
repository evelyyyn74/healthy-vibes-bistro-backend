// NegocioRepository.java
package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegocioRepository extends JpaRepository<Negocio, Long> { }