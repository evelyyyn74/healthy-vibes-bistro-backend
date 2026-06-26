package com.hvb.loyalty.dto;

public class DashboardResponseDTO {

    private long totalClientes;
    private long clientesActivos;
    private long totalTarjetas;
    private long totalPremios;
    private long totalCanjes;

    public long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(long totalClientes) { this.totalClientes = totalClientes; }

    public long getClientesActivos() { return clientesActivos; }
    public void setClientesActivos(long clientesActivos) { this.clientesActivos = clientesActivos; }

    public long getTotalTarjetas() { return totalTarjetas; }
    public void setTotalTarjetas(long totalTarjetas) { this.totalTarjetas = totalTarjetas; }

    public long getTotalPremios() { return totalPremios; }
    public void setTotalPremios(long totalPremios) { this.totalPremios = totalPremios; }

    public long getTotalCanjes() { return totalCanjes; }
    public void setTotalCanjes(long totalCanjes) { this.totalCanjes = totalCanjes; }
}