package com.upload.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produto_atendimento")
public class ProdutoAtendimento {
    
    @EmbeddedId
    private ProdutoAtendimentoId id = new ProdutoAtendimentoId();

    @ManyToOne
    @MapsId("atendimentoId")
    private Atendimento atendimento;

    @ManyToOne
    @MapsId("produtoId")
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;
}
