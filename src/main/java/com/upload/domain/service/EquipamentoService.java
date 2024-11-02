package com.upload.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.upload.domain.exception.EntidadeNaoEncontradaException;
import com.upload.domain.exception.EntidadeNaoRemoverException;
import com.upload.domain.exception.RegistroJaCadastradoException;
import com.upload.domain.model.Equipamento;
import com.upload.domain.repository.EquipamentoRepository;

@Service
public class EquipamentoService {
    
    @Autowired
    private EquipamentoRepository repository;

    @Transactional
    public Equipamento salvar(Equipamento equipamento) {
        if (equipamento.getId() == null) {
            validaNumSerieUnico(equipamento);
        }
        return repository.save(equipamento);
    }

    public Equipamento buscar(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Não existe equipamento com ID %d", id)));
    }

    public Equipamento buscarEquipamento(String numSerie) {
        return repository.findByNumSerie(numSerie);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            Equipamento equipamento = buscar(id);
            repository.delete(equipamento);
        } catch (Exception e) {
            throw new EntidadeNaoRemoverException(e.getMessage());
        }
    }

    private void validaNumSerieUnico(Equipamento equipamento) {
        if (repository.findByNumSerie(equipamento.getNumSerie()) != null) {
            var mensagem = String.format("O número de série %s já está cadastrado", equipamento.getNumSerie());
            var fieldError = new FieldError(equipamento.getClass().getName(),
                "codigo", equipamento.getNumSerie(), false, null, null, mensagem);
            
            throw new RegistroJaCadastradoException(mensagem, fieldError);
        }
    }
}
