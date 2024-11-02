package com.upload.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.upload.domain.model.Cliente;
import com.upload.domain.repository.ClienteRepository;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository repository;

    public void salvar(Cliente cliente) {
        repository.save(cliente);
    }

    public void excluir(Long id) {
        try {
            var clienteEncontrado = buscarPorId(id);
            repository.delete(clienteEncontrado);
        } catch (Exception e) {
            throw new IllegalArgumentException("O registro não pode ser removido");
        }
    }

    public List<Cliente> lista() {
        List<Cliente> clientes = repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
		return clientes;
	}

    public Page<Cliente> listarTodos(int numPage) {
        int size = 4;
        Pageable pageable = PageRequest.of(numPage -1, size, Sort.by("nome"));
        return repository.findAll(pageable);
    }

    public Page<Cliente> buscarPorNome(String nome, int numPage) {
        int size = 5;
        Pageable pageable = PageRequest.of(numPage -1, size, Sort.by("nome"));
        return repository.findByNome(nome, pageable);
    }

    public Cliente buscarPorId(Long id) {
        Optional<Cliente> clienteEncontrado = repository.findById(id);

        if (clienteEncontrado.isPresent()) {
            return clienteEncontrado.get();
        } else {
            return null;
        }
    }

    public Cliente buscarPorCpf(String cpf) {
        Cliente cliente = repository.findByCpf(cpf);
        return cliente;
    }

    public Cliente buscarPorEmail(String email) {
        Cliente cliente = repository.findByEmail(email);
        return cliente;
    }
}
