package entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import entities.enums.StatusFrete;
import entities.enums.TipoVeiculo;

public class Frete {
    private Interger id;
    private Date data;
    private StatusFrete status;
    private TipoVeiculo tipoVeiculo;
    private Rota rota;
    private Cliente cliente;
    private Double distancia;
    private Double valorPorKm;

    private static SimpleDateFormat sdf = new simpleDateFormat ("dd/MM/yyyy");
    private static Integer proximoId = 1;

    public Frete(Date data, StatusFrete status, TipoVeiculo tipoVeiculo, Rota rota, Cliente cliente, Double distancia, Double valorPorKm) {
        this.id = proximoId++;
        this.data = data;
        this.status = status;
        this.tipoVeiculo = tipoVeiculo;
        this.rota = rota;
        this.cliente = cliente;
        this.distancia = distancia;
        this.valorPorKm = valorPorKm;
    }

    public Integer getId() {
        return id;
    }
    public Date getData() {
        return data;
    }
    public StatusFrete getStatus() {
        return status;
    }
    public void setStatus(StatusFrete status) {
        this.status = status;
    }
    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }
    public Rota getRota() {
        return rota;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Double getDistancia() {
        return distancia;
    }
    public Double getValorPorKm() {
        return valorPorKm;
    }
    public Double getDesconto() {
        return tipoVeiculo.getDiscount();
    }

    public Double valorTotal() {
        Double valorBase = distancia * valorPorKm;
        Double desconto = valorBase * getDesconto();
        return valorBase - desconto;
    }

    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Frete #").append(id).apend("\n");
        sb.append(" Data: ").append(sdf.format(data)).append("\n");
        sb.append(" Cliente: ").append(cliente).append("\n");
        sb.append(" Rota: ").append(rota).append("\n");
        sb.append("  Distância: ").append(String.format("%.0f", distancia)).append(" km\n");
        sb.append("  Tipo: ").append(tipoVeiculo).append("\n");
        sb.append("  Status: ").append(status).append("\n");
        sb.append("  Valor/km: R$ ").append(String.format("%.2f", valorPorKm)).append("\n");
        sb.append("  Desconto: ").append(String.format("%.0f", getDesconto() * 100)).append("%\n");
        sb.append("  Valor Total: R$ ").append(String.format("%.2f", valorTotal()));
        return sb.toString();
    }
    
}
