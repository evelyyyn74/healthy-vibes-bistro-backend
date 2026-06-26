package com.hvb.loyalty.dto;

import java.time.LocalDate;

public class TransaccionResponseDTO {

    private Long id;
    private String tipo;
    private Integer puntos;
    private String descripcion;
    private String categoria;
    private LocalDate fecha;
    private Long tarjetaId;
    private String usuarioNombre; // para mostrar quién la registró

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Long getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(Long tarjetaId) { this.tarjetaId = tarjetaId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
}