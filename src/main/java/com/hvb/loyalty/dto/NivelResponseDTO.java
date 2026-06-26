package com.hvb.loyalty.dto;

public class NivelResponseDTO {

    private Long id;
    private String nombre;
    private Integer puntosMin;
    private Integer puntosMax;
    private Integer orden;
    private Integer puntosPorVisita;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getPuntosMin() { return puntosMin; }
    public void setPuntosMin(Integer puntosMin) { this.puntosMin = puntosMin; }

    public Integer getPuntosMax() { return puntosMax; }
    public void setPuntosMax(Integer puntosMax) { this.puntosMax = puntosMax; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Integer getPuntosPorVisita() { return puntosPorVisita; }
    public void setPuntosPorVisita(Integer puntosPorVisita) { this.puntosPorVisita = puntosPorVisita; }
}