package com.hvb.loyalty.dto;

public class CanjeRequestDTO {

    private Integer puntosDescontados;
    private Long tarjetaId;
    private Long usuarioId;
    private Long premioId;

    public Integer getPuntosDescontados() { return puntosDescontados; }
    public void setPuntosDescontados(Integer puntosDescontados) { this.puntosDescontados = puntosDescontados; }

    public Long getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(Long tarjetaId) { this.tarjetaId = tarjetaId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getPremioId() { return premioId; }
    public void setPremioId(Long premioId) { this.premioId = premioId; }
}