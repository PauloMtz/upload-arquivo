package com.upload.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.upload.domain.model.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    @Query("from Atendimento a join fetch a.ordemServico where a.ordemServico.status = 'OS_ATENDIDA'")
    List<Atendimento> listarAtendimentos();
}
