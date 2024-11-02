package com.upload.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "enderecos")
public class Endereco {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotBlank(message = "Preencha o campo CEP")
    @Column(name = "cep", nullable = false, length = 9)
    private String cep;
    
    @NotBlank(message = "Informe o endereço")
    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "complemento")
    private String complemento;

    @NotBlank(message = "Preencha o campo bairro")
    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @NotBlank(message = "Preencha o campo cidade")
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "Informe um estado")
    @Column(name = "estado", length = 2, nullable = false)
    private String uf;

    // não é necessário, caso não precise chegar ao cliente a partir de endereço
    /*@OneToOne(mappedBy = "endereco")
    private Cliente cliente;*/
}
