package com.hvb.loyalty.dto;

import java.time.LocalDate;

public class PremioResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer puntosRequeridos;
    private Integer limiteCanjes;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean disponible;
    private String aplicaPara;
    private Integer canjesRealizados;
    private NivelResponseDTO nivel; // objeto completo

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Integer getCanjesRealizados() { return canjesRealizados; }
    public void setCanjesRealizados(Integer canjesRealizados) { this.canjesRealizados = canjesRealizados; }

    public NivelResponseDTO getNivel() { return nivel; }
    public void setNivel(NivelResponseDTO nivel) { this.nivel = nivel; }
}