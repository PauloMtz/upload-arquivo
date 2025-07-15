package com.upload.web.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.upload.domain.exception.EntidadeNaoEncontradaException;
import com.upload.domain.model.Equipamento;
import com.upload.domain.repository.EquipamentoRepository;
import com.upload.domain.service.EquipamentoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/equipamento")
public class EquipamentoController {

    @Autowired
    private EquipamentoRepository repository;

    @Autowired
    private EquipamentoService service;
    
    @GetMapping("/listar")
    public ModelAndView listar() {
        var mv = new ModelAndView("equipamentos/lista");
        mv.addObject("equipamentos", repository.findAll());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView carregaCadastro() {
        var mv = new ModelAndView("equipamentos/form");
        mv.addObject("equipamento", new Equipamento());
        return mv;
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid Equipamento equipamento, BindingResult result,
        RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "equipamentos/form";
        }

        Equipamento equip = service.buscarEquipamento(equipamento.getNumSerie());

        if (equip != null) {
            attr.addFlashAttribute("error", 
                String.format("Já existe um equipamento com o número de série '%s'", equip.getNumSerie()));
            return "redirect:/equipamento/cadastrar";
        }

        equipamento.setDataCriacao(LocalDateTime.now());
        service.salvar(equipamento);
        attr.addFlashAttribute("success", "Cadastro efetuado com sucesso.");
        return "redirect:/equipamento/cadastrar";
    }

    @GetMapping("/buscar")
    public String buscarEquipamento(@RequestParam("numSerie") String numSerie,
        RedirectAttributes attr, Model model) {

        Equipamento equip = repository.findByNumSerie(numSerie);

        if (equip == null) {
            attr.addFlashAttribute("error", 
                String.format("Não existe equipamento com número de série '%s'", numSerie));
            return "redirect:/equipamento/listar";
        }
        
        model.addAttribute("equipamentos", equip);
        return "equipamentos/lista";
    }

    @GetMapping("/buscar-equipamento")
    public ResponseEntity<?> buscarEquipamento(RedirectAttributes attr, 
        @RequestParam("numSerie") String numSerie) {

        Equipamento equipamento = service.buscarEquipamento(numSerie);

        if (equipamento == null) {
            throw new EntidadeNaoEncontradaException("\nEquipamentoController - buscar-equipamento");
        }

        return ResponseEntity.ok(equipamento);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        
        Optional<Equipamento> equip = repository.findById(id);
        
        if (!equip.isPresent()) {
            attr.addFlashAttribute("error", String.format("Não existe equipamento com ID %d", id));
            return "redirect:/equipamento/listar";
        }
        
        model.addAttribute("equipamento", equip);
        return "equipamentos/form";
    }

    @PostMapping("/editar/{id}")
    public String editar(@Valid @ModelAttribute("equipamento") Equipamento equipamento, 
        Model model, @PathVariable("id") Long id, BindingResult result, 
        RedirectAttributes attr) throws IOException {

        if (result.hasErrors()) {
            equipamento.setId(id);
            return "equipamentos/form";
        }

        Equipamento equip = service.buscarEquipamento(equipamento.getNumSerie());

        if (equip != null && equip.getId() != id) {
            attr.addFlashAttribute("error", 
                String.format("Já existe um equipamento com o número de série '%s'", equip.getNumSerie()));
            return "redirect:/equipamento/editar/" + id;
        }

        equipamento.setDataAtualizacao(LocalDateTime.now());
        service.salvar(equipamento);
        attr.addFlashAttribute("success", "Registro atualizado com sucesso.");
        return "redirect:/equipamento/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attr) {
        try {
            service.excluir(id);
            attr.addFlashAttribute("success", "Registro excluído com sucesso.");
            return "redirect:/equipamento/listar";
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Esse registro não pode ser excluído");
            return "redirect:/equipamento/listar";
        }
    }
}
