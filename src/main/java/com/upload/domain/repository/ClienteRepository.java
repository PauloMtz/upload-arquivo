package com.upload.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.upload.domain.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);
    Cliente findByEmail(String email);
    
    @Query("FROM Cliente f WHERE UPPER(f.nome) LIKE CONCAT('%', UPPER(:nome), '%')")
    Page<Cliente> findByNome(@Param("nome") String nome, Pageable pageable);
}
