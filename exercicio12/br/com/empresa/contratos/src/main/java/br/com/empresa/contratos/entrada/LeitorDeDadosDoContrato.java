package br.com.empresa.contratos.entrada;

import br.com.empresa.contratos.dominio.Contrato;

public interface LeitorDeDadosDoContrato {

    Contrato lerContrato();

    int lerQuantidadeDeParcelas();
}