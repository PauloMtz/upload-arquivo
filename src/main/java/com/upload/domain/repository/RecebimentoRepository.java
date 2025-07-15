package com.upload.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.upload.domain.model.Equipamento;
import com.upload.domain.model.Recebimento;

public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {

    @Query("from Recebimento r where r.status = 'RECEBE_EQUIPAMENTO'")
    List<Recebimento> listarRecebimentos();

    @Query("SELECT count(*) FROM Recebimento r WHERE r.status = 'RECEBE_EQUIPAMENTO'")
    long countByEquipamento();

    Recebimento findByEquipamento(Equipamento equipamento);
}
