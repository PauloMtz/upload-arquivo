package com.upload.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.upload.domain.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "ordens_servico")
public class OrdemServico {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "numero_os")
	private String numeroOs;

    @NotBlank
    @Column(name = "descricao")
	private String descricao;

    @NotNull
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(columnDefinition = "DECIMAL(9, 2) DEFAULT 0.00")
	private BigDecimal valor;

    @PastOrPresent
    @Column(name = "data_ordem_servico", updatable = false, nullable = false)
    private LocalDateTime dataOrdemServico;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "recebimento_id")
    private Recebimento recebimento;

    @ToString.Exclude
    @Valid
    @OneToOne
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    // altera o status da OS
    public void abreOrdemServico() {
        setStatus(Status.OS_ABERTA);
        setDataOrdemServico(LocalDateTime.now());
    }

    public void atendeOrdemServico() {
        setStatus(Status.OS_ATENDIDA);
    }
}
