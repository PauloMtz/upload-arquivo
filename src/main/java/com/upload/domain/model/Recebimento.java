package com.upload.domain.model;

import java.time.LocalDateTime;

import com.upload.domain.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "recebimentos")
public class Recebimento {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @PastOrPresent
    @Column(name = "data_recebimento", updatable = false, nullable = false)
    private LocalDateTime dataRecebimento;

    @Column(name = "observacao", columnDefinition="TEXT")
	private String observacao;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Valid
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    @Valid
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne(mappedBy = "recebimento")
    private OrdemServico ordemServico;

    // altera o status
    public void receberEquipamento() {
        setStatus(Status.RECEBE_EQUIPAMENTO);
        setDataRecebimento(LocalDateTime.now());
    }

    public void efetuarAtendimento() {
        setStatus(Status.EFETUA_ATENDIMENTO);
    }

    public void abreOrdemServico() {
        setStatus(Status.ABRE_ORDEM_SERVICO);
    }
}
