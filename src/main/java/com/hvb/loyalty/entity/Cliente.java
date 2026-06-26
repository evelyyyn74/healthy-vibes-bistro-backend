package com.hvb.loyalty.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 15)
    private String telefono;

    @Column(unique = true, length = 100)
    private String correo;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "nivel_id")
    private Nivel nivel;

    @Column(name = "fecha_ultimo_reinicio")
    private java.time.LocalDateTime fechaUltimoReinicio;

    @PrePersist
    protected void onCreate() {
        if (this.fechaRegistro == null) this.fechaRegistro = LocalDate.now();
        if (this.activo == null) this.activo = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public java.time.LocalDateTime getFechaUltimoReinicio() { return fechaUltimoReinicio; }
    public void setFechaUltimoReinicio(java.time.LocalDateTime f) { this.fechaUltimoReinicio = f; }
}