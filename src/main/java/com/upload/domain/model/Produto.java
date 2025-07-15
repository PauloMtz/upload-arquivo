package com.upload.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Entity
@Table(name = "produtos")
public class Produto {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotBlank
    @Column(name = "codigo", nullable = false, length = 10)
	private String codigo;

    @NotBlank
    @Column(name = "nome", nullable = false, length = 60)
	private String nome;

    @NotBlank
    @Column(name = "descricao", nullable = false, length = 100)
	private String descricao;

    @Column(length = 100)
    private String foto;

    @NotNull
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(columnDefinition = "DECIMAL(9, 2) DEFAULT 0.00")
	private BigDecimal preco;

    @Column(nullable = false)
	private Integer saldo = 0;

    @Column(nullable = false)
	private boolean ativo;

    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_atualizacao", insertable = false)
    private LocalDateTime dataAtualizacao;

    @JsonIgnore
    @OneToMany(mappedBy = "produto")
    private List<Entrada> entradas;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoAtendimento> atendimentos = new ArrayList<>();

    public Integer incrementaSaldo(Integer valor) {
        this.saldo = this.saldo + valor;
        return this.saldo;
    }

    public Integer baixaSaldo(Integer valor) {
        this.saldo = this.saldo - valor;
        return this.saldo;
    }
}
