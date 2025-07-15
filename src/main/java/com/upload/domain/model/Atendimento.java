package com.upload.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "atendimentos")
public class Atendimento {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "descricao", columnDefinition="TEXT")
	private String descricao;

    @PastOrPresent
    @Column(name = "data_atendimento", updatable = false, nullable = false)
    private LocalDateTime dataAtendimento;

    @OneToMany(mappedBy = "atendimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoAtendimento> produtos = new ArrayList<>();

    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(columnDefinition = "DECIMAL(9,2) DEFAULT 0.00")
    private BigDecimal valorTotal;

    @OneToOne
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;

    public void calcularValorTotal() {
        BigDecimal totalProdutos = produtos.stream()
            .map(p -> p.getProduto().getPreco().multiply(BigDecimal.valueOf(p.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = totalProdutos.add(
            ordemServico != null && ordemServico.getValor() != null
                ? ordemServico.getValor()
                : BigDecimal.ZERO
        );
    }
}
