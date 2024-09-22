package com.upload.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.domain.model.Entrada;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
}
