package com.hvb.loyalty.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "niveles")
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "puntos_min")
    private Integer puntosMin;

    @Column(name = "puntos_max")
    private Integer puntosMax;

    private Integer orden;

    @Column(name = "puntos_por_visita")
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