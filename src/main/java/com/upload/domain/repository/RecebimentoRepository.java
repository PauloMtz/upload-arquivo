package com.upload.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.domain.model.Equipamento;
import com.upload.domain.model.Recebimento;

public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {

    Recebimento findByEquipamento(Equipamento equipamento);
}
