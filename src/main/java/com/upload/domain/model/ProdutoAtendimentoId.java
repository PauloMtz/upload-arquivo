package com.upload.domain.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoAtendimentoId implements Serializable {
    
    private Long atendimentoId;
    private Long produtoId;
}
