package br.com.empresa.contratos.processamento;

import br.com.empresa.contratos.dominio.Contrato;

public interface ProcessadorDeContrato {

    void processar(Contrato contrato, int quantidadeDeParcelas);
}