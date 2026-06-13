package com.hvb.loyalty.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tarjetas")
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String serial;

    @Column(name = "codigo_qr", nullable = false, unique = true)
    private String codigoQr;

    @Column(nullable = false)
    private Boolean activa;

    @Column(name = "apple_pass_serial")
    private String applePassSerial;

    @Column(name = "google_object_id")
    private String googleObjectId;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.serial == null) this.serial = UUID.randomUUID().toString();
        if (this.codigoQr == null) this.codigoQr = UUID.randomUUID().toString();
        if (this.activa == null) this.activa = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public String getApplePassSerial() { return applePassSerial; }
    public void setApplePassSerial(String applePassSerial) { this.applePassSerial = applePassSerial; }

    public String getGoogleObjectId() { return googleObjectId; }
    public void setGoogleObjectId(String googleObjectId) { this.googleObjectId = googleObjectId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}