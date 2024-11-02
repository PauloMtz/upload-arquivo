package com.upload.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"dataCriacao", "dataAtualizacao"})
@Getter
@Setter
@Entity
@Table(name = "equipamentos")
public class Equipamento {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotBlank
    @Column(name = "descricao", nullable = false, length = 60)
	private String descricao;

    @NotBlank
    @Column(name = "marca", nullable = false, length = 60)
	private String marca;

    @NotBlank
    @Column(name = "modelo", nullable = false, length = 60)
	private String modelo;

    @NotBlank
    @Column(name = "num_serie", nullable = false, length = 20)
	private String numSerie;

    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_atualizacao", insertable = false)
    private LocalDateTime dataAtualizacao;
}
