// TransaccionRepository.java
package com.hvb.loyalty.repository;

import com.hvb.loyalty.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> { }