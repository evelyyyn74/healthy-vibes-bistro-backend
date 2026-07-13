package com.hvb.loyalty.dto;

public class TarjetaResponseDTO {

    private Long id;
    private String codigoQr;
    private Integer puntosAcumulados;
    private Integer puntosCanjeados;
    private Boolean activo;
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellidos;
    private String clienteTelefono;
    private String clienteCorreo;
    private String nivelNombre;
    private Integer nivelPuntosMax;
    private Integer puntosParaRecompensa;
    private Integer recompensasObtenidas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }

    public Integer getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(Integer puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    public Integer getPuntosCanjeados() { return puntosCanjeados; }
    public void setPuntosCanjeados(Integer puntosCanjeados) { this.puntosCanjeados = puntosCanjeados; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteApellidos() { return clienteApellidos; }
    public void setClienteApellidos(String clienteApellidos) { this.clienteApellidos = clienteApellidos; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

    public String getClienteCorreo() { return clienteCorreo; }
    public void setClienteCorreo(String clienteCorreo) { this.clienteCorreo = clienteCorreo; }

    public String getNivelNombre() { return nivelNombre; }
    public void setNivelNombre(String nivelNombre) { this.nivelNombre = nivelNombre; }

    public Integer getNivelPuntosMax() { return nivelPuntosMax; }
    public void setNivelPuntosMax(Integer nivelPuntosMax) { this.nivelPuntosMax = nivelPuntosMax; }

    public Integer getPuntosParaRecompensa() { return puntosParaRecompensa; }
    public void setPuntosParaRecompensa(Integer puntosParaRecompensa) { this.puntosParaRecompensa = puntosParaRecompensa; }

    public Integer getRecompensasObtenidas() { return recompensasObtenidas; }
    public void setRecompensasObtenidas(Integer recompensasObtenidas) { this.recompensasObtenidas = recompensasObtenidas; }
}