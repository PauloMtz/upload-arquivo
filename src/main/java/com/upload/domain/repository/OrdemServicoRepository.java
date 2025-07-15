package com.upload.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.upload.domain.model.OrdemServico;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    Optional<OrdemServico> findByNumeroOs(String numeroOs);

    @Query("from OrdemServico os where os.status = 'OS_ABERTA'")
    List<OrdemServico> listarOS();

    @Query("SELECT count(*) FROM OrdemServico os WHERE os.status = 'OS_ABERTA'")
    long countById();
}
