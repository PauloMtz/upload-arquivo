package com.upload.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false, length = 60)
	private String nome;

    @CPF
    @NotNull
    @Column(name = "cpf", nullable = false, length = 11)
	private String cpf;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, length = 60)
	private String email;

    @NotBlank
    @Column(name = "telefone", nullable = false, length = 15)
	private String telefone;

    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_atualizacao", insertable = false)
    private LocalDateTime dataAtualizacao;

    // valida os atributos de endereço
    @Valid
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Recebimento> recebimentos;
}
