package com.upload.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upload.domain.model.OrdemServico;
import com.upload.domain.repository.OrdemServicoRepository;

@Service
public class OrdemServicoService {
    
    @Autowired
    private OrdemServicoRepository repository;

    public void salvar(OrdemServico ordemServico) {
        repository.save(ordemServico);
    }
}
