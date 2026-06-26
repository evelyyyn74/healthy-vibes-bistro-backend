package com.hvb.loyalty.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class PremioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
    private Integer puntosRequeridos;
    private Integer limiteCanjes;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean disponible;
    private String aplicaPara;
    private Long nivelId; // solo el ID

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getPuntosRequeridos() { return puntosRequeridos; }
    public void setPuntosRequeridos(Integer puntosRequeridos) { this.puntosRequeridos = puntosRequeridos; }

    public Integer getLimiteCanjes() { return limiteCanjes; }
    public void setLimiteCanjes(Integer limiteCanjes) { this.limiteCanjes = limiteCanjes; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public String getAplicaPara() { return aplicaPara; }
    public void setAplicaPara(String aplicaPara) { this.aplicaPara = aplicaPara; }

    public Long getNivelId() { return nivelId; }
    public void setNivelId(Long nivelId) { this.nivelId = nivelId; }
}