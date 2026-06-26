package com.hvb.loyalty.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tarjetas")
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "nivel_id")
    private Nivel nivel;

    @Column(name = "codigo_qr", unique = true)
    private String codigoQr;

    @Column(name = "puntos_acumulados")
    private Integer puntosAcumulados;

    @Column(name = "puntos_canjeados")
    private Integer puntosCanjeados;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_creacion", updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    @Column(name = "serial")
    private String serial;

    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = java.time.LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }

    public Integer getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(Integer puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    public Integer getPuntosCanjeados() { return puntosCanjeados; }
    public void setPuntosCanjeados(Integer puntosCanjeados) { this.puntosCanjeados = puntosCanjeados; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

}