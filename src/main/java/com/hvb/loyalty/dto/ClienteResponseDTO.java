package com.hvb.loyalty.dto;

import com.hvb.loyalty.entity.NivelCliente;
import java.time.LocalDateTime;

public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private NivelCliente nivel;
    private Integer puntos;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimoReinicio;
    private String codigoQr;

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

    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
}