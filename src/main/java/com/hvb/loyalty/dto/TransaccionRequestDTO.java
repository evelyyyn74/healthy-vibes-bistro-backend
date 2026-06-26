package com.hvb.loyalty.dto;

import jakarta.validation.constraints.NotBlank;

public class TransaccionRequestDTO {

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo; // VISITA, COMPRA, CANJE

    private Integer puntos;
    private String descripcion;
    private String categoria;
    private Long tarjetaId;
    private Long usuarioId;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Long getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(Long tarjetaId) { this.tarjetaId = tarjetaId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}