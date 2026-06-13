package com.hvb.loyalty.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(length = 15)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelCliente nivel;

    @Column(nullable = false)
    private Integer puntos;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_ultimo_reinicio")
    private LocalDateTime fechaUltimoReinicio;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Tarjeta tarjeta;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.puntos == null) this.puntos = 0;
        if (this.nivel == null) this.nivel = NivelCliente.NUEVO;
        if (this.activo == null) this.activo = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public NivelCliente getNivel() { return nivel; }
    public void setNivel(NivelCliente nivel) { this.nivel = nivel; }

    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getFechaUltimoReinicio() { return fechaUltimoReinicio; }
    public void setFechaUltimoReinicio(LocalDateTime fechaUltimoReinicio) { this.fechaUltimoReinicio = fechaUltimoReinicio; }

    public Tarjeta getTarjeta() { return tarjeta; }
    public void setTarjeta(Tarjeta tarjeta) { this.tarjeta = tarjeta; }
}