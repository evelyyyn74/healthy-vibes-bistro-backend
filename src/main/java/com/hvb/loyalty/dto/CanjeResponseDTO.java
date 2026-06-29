package com.hvb.loyalty.dto;

import java.time.LocalDate;

public class CanjeResponseDTO {

    private Long id;
    private Integer puntosDescontados;
    private LocalDate fecha;
    private Long tarjetaId;
    private String usuarioNombre;
    private String premioNombre;
    private String clienteNombre;
    private String clienteNivel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPuntosDescontados() { return puntosDescontados; }
    public void setPuntosDescontados(Integer puntosDescontados) { this.puntosDescontados = puntosDescontados; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Long getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(Long tarjetaId) { this.tarjetaId = tarjetaId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getPremioNombre() { return premioNombre; }
    public void setPremioNombre(String premioNombre) { this.premioNombre = premioNombre; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteNivel() { return clienteNivel; }
    public void setClienteNivel(String clienteNivel) { this.clienteNivel = clienteNivel; }
}