package com.upload.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.upload.domain.service.OrdemServicoService;

@Controller
@RequestMapping("/ordem-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;
    
    @RequestMapping("/listar")
    public ModelAndView index() {
        var mv = new ModelAndView("operacoes/lista");
        mv.addObject("ordensServico", ordemServicoService.lista());
        return mv;
    }
}
