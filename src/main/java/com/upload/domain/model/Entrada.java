package com.upload.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Entity
@Table(name = "entrada_itens")
public class Entrada {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotNull
    @Column(nullable = false)
	private Integer quantidade;

    @NotNull
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(columnDefinition = "DECIMAL(9, 2) DEFAULT 0.00")
	private BigDecimal preco;

    @Valid
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @PastOrPresent
    @Column(name = "data_atualizacao", insertable = true)
    private LocalDateTime dataAtualizacao;
}
