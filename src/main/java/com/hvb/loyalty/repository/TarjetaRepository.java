package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    Optional<Tarjeta> findByCodigoQr(String codigoQr);
}