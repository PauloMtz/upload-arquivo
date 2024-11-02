package com.upload.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.domain.model.Equipamento;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    Equipamento findByNumSerie(String numSerie);
}
