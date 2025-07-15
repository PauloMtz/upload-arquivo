package com.upload.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upload.domain.model.OrdemServico;
import com.upload.domain.model.Recebimento;
import com.upload.domain.model.enums.Status;
import com.upload.domain.repository.OrdemServicoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrdemServicoService {
    
    @Autowired
    private OrdemServicoRepository repository;

    public void salvar(OrdemServico ordemServico) {
        repository.save(ordemServico);
    }

    public List<OrdemServico> lista() {
        List<OrdemServico> ordensServico = repository.listarOS();
		return ordensServico;
	}

    public OrdemServico buscarOrdemServico(String numeroOs) {
        Optional<OrdemServico> registroEncontrado = repository.findByNumeroOs(numeroOs);

        if (registroEncontrado.isPresent()) {
            return registroEncontrado.get();
        } else {
            return null;
        }
    }

    // classe sugerida pelo chatGPT
    @Transactional
    public void atualizaStatusRecebimento(String numeroOs) {
        Optional<OrdemServico> registroEncontrado = repository.findByNumeroOs(numeroOs);

        if (registroEncontrado.isPresent()) {
            OrdemServico ordemServico = registroEncontrado.get();

            Recebimento recebimento = ordemServico.getRecebimento();
            if (recebimento != null) {
                Status statusAtual = recebimento.getStatus();
                Status novoStatus = Status.EFETUA_ATENDIMENTO;

                if (statusAtual.naoPodeAlterarStatus(novoStatus)) {
                    throw new IllegalStateException("Status inválido para transição: de " 
                        + statusAtual + " para " + novoStatus);
                }

                recebimento.efetuarAtendimento(); // altera o status para EFETUA_ATENDIMENTO
            } else {
                throw new IllegalStateException("Recebimento não vinculado à OS.");
            }
        } else {
            throw new EntityNotFoundException("Ordem de serviço não encontrada para o número: " + numeroOs);
        }
    }

    public long contadorOS() {
        return repository.countById();
    }
}
