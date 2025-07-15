package com.upload.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upload.domain.model.Equipamento;
import com.upload.domain.model.Recebimento;
import com.upload.domain.repository.RecebimentoRepository;

@Service
public class RecebimentoService {
    
    @Autowired
    private RecebimentoRepository repository;

    public void salvar(Recebimento recebimento) {
        repository.save(recebimento);
    }

    public Recebimento buscarEquipamento(Equipamento equipamento) {
        Recebimento recebimento = repository.findByEquipamento(equipamento);

        return recebimento;
    }

    public List<Recebimento> lista() {
        List<Recebimento> equipamentos = repository.listarRecebimentos();
		return equipamentos;
	}

    public Long contadorRecebimentos() {
        return repository.countByEquipamento();
    }
}
