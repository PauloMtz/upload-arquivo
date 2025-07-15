package com.upload.web.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.upload.domain.exception.ArquivoNaoEncontradoException;
import com.upload.domain.model.Cliente;
import com.upload.domain.service.ClienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteService service;

    @GetMapping("/listar")
    public String listar(Model model) {
        return listaPaginada(model, 1);
    }

    @GetMapping("/listar/{pageNumber}")
    public String listaPaginada(Model model,
        @PathVariable(value = "pageNumber") int currentPage) {

        Page<Cliente> page = service.listarTodos(currentPage);
        List<Cliente> clientes = page.getContent();
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        String url = "/cliente/listar/";
        String pag = "";

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("url", url);
        model.addAttribute("pag", pag);
        model.addAttribute("clientes", clientes);

        return "clientes/lista";
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        var mv = new ModelAndView("clientes/form");
        mv.addObject("cliente", new Cliente());
        return mv;
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid Cliente cliente, BindingResult result,
            Model model, RedirectAttributes attr) throws IOException {

        if (result.hasErrors()) {
            return "clientes/form";
        }

        Cliente cliCpf = service.buscarPorCpf(cliente.getCpf());
        Cliente cliEmail = service.buscarPorEmail(cliente.getEmail());

        if (cliCpf != null) {
            model.addAttribute("cpfExiste", "CPF já cadastrado no sistema.");
            return "clientes/form";
        }

        if (cliEmail != null) {
            model.addAttribute("emailExiste", "E-mail já cadastrado no sistema.");
            return "admin/clientes/form";
        }

        cliente.setDataCriacao(LocalDateTime.now());
        service.salvar(cliente);
        attr.addFlashAttribute("success", "Registro inserido com sucesso.");
        return "redirect:/cliente/listar";
    }

    @GetMapping("/buscar")
    public String getPorNome(String nome, Model model) {
        return buscaPaginada(model, nome, 1);
    }

    
    @GetMapping("/buscar/{pageNumber}")
    public String buscaPaginada(Model model, @RequestParam("nome") String nome,
        @PathVariable(value = "pageNumber") int currentPage) {

        Page<Cliente> page = service.buscarPorNome(nome, currentPage);
        List<Cliente> clientes = page.getContent();
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        String url = "/cliente/buscar/";
        String pag = "?nome=" + nome;

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("url", url);
        model.addAttribute("pag", pag);
        model.addAttribute("clientes", clientes);

        return "clientes/lista";
    }

    @GetMapping("/buscar-cliente")
    public ResponseEntity<?> buscarCliente(RedirectAttributes attr, 
        @RequestParam("id") String id) throws Exception {

        try {
            Cliente cliente = service.buscarPorId(Long.parseLong(id));
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
            throw new ArquivoNaoEncontradoException("\n>>> Classe ClienteController: Não encontrado");
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Cliente clienteEncontrado = service.buscarPorId(id);

        if (clienteEncontrado == null) {
            attr.addFlashAttribute("error", "Cliente não encontrado");
            return "redirect:/cliente/listar";
        }

        model.addAttribute("cliente", clienteEncontrado);
        return "clientes/form";
    }

    @PostMapping("/editar/{id}")
    public String editar(@Valid @ModelAttribute("cliente") Cliente cliente, Model model,
            @PathVariable("id") Long id, BindingResult result, RedirectAttributes attr) throws IOException {

        if (result.hasErrors()) {
            cliente.setId(id);
            return "clientes/form";
        }

        Cliente cliCpf = service.buscarPorCpf(cliente.getCpf());
        Cliente cliEmail = service.buscarPorEmail(cliente.getEmail());

        if (cliCpf != null && cliCpf.getId() != id) {
            attr.addFlashAttribute("cpfExiste", "CPF já cadastrado no sistema.");
            return "redirect:/cliente/editar/" + id;
        }

        if (cliEmail != null && cliEmail.getId() != id) {
            attr.addFlashAttribute("emailExiste", "E-mail já cadastrado no sistema.");
            return "redirect:/cliente/editar/" + id;
        }

        cliente.setDataAtualizacao(LocalDateTime.now());
        service.salvar(cliente);
        attr.addFlashAttribute("success", "Registro atualizado com sucesso.");
        return "redirect:/cliente/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attr) {
        try {
            service.excluir(id);
            attr.addFlashAttribute("success", "Registro excluído com sucesso.");
            return "redirect:/cliente/listar";
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Esse registro não pode ser excluído");
            return "redirect:/cliente/listar";
        }
    }
}
