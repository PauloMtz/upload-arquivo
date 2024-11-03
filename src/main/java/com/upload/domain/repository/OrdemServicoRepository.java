package com.upload.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.domain.model.OrdemServico;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
}
