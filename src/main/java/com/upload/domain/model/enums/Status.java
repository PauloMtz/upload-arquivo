package com.upload.domain.model.enums;

import java.util.Arrays;
import java.util.List;

public enum Status {
    
    // cada status terá seu(s) anterior(es) informado(s)
    RECEBE_EQUIPAMENTO("Equipamento Recebido"), // quando recebe
    ABRE_ORDEM_SERVICO("OS Aberta", RECEBE_EQUIPAMENTO), // quando abre OS
    EFETUA_ATENDIMENTO("OS Atendida", ABRE_ORDEM_SERVICO), // quando atende OS
    CANCELA_ATENDIMENTO("Atendimento Cancelado", RECEBE_EQUIPAMENTO, ABRE_ORDEM_SERVICO), // quando atende OS ou recebimento
    DEVOLVE_EQUIPAMENTO("Equipamento Devolvido", EFETUA_ATENDIMENTO, CANCELA_ATENDIMENTO), // quando devolve

    OS_ABERTA("Ordem de serviço aberta"),
    OS_ATENDIDA("Ordem de serviço atendida", OS_ABERTA), // status atual e anterior
    OS_FECHADA("Ordem de serviço fechada", OS_ABERTA, OS_ATENDIDA),
    OS_CANCELADA("Ordem de serviço cancelada", OS_ABERTA);

    // o construtor passa a receber um var args (uma lista)
    Status(String descricao, Status... statusAnteriores) {
        this.descricao = descricao;
        this.statusAnteriores = Arrays.asList(statusAnteriores);
    }

    private String descricao;
    private List<Status> statusAnteriores;

    public String getDescricao() {
        return this.descricao;
    }

    public boolean naoPodeAlterarStatus(Status novoStatus) {
		// se o novo status não estiver na lista (var args), chama esse método
		return !novoStatus.statusAnteriores.contains(this);
	}
}
