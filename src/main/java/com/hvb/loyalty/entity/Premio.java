package com.hvb.loyalty.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "premios")
public class Premio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nivel_id")
    private Nivel nivel;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "puntos_requeridos")
    private Integer puntosRequeridos;

    @Column(name = "limite_canjes")
    private Integer limiteCanjes;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private Boolean disponible;

    @Column(name = "aplica_para", length = 50)
    private String aplicaPara;

    @Column(name = "canjes_realizados")
    private Integer canjesRealizados;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

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
}