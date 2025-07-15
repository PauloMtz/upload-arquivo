package com.upload.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upload.domain.model.Atendimento;
import com.upload.domain.repository.AtendimentoRepository;

@Service
public class AtendimentoService {
    
    @Autowired
    private AtendimentoRepository repository;

    public void salvar(Atendimento atendimento) {
        repository.save(atendimento);
    }

    public List<Atendimento> lista() {
        List<Atendimento> atendimentos = repository.listarAtendimentos();
		return atendimentos;
	}
}
