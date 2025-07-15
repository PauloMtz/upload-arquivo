package com.upload.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.upload.domain.service.ClienteService;
import com.upload.domain.service.OrdemServicoService;
import com.upload.domain.service.RecebimentoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class HomeController {

    private final RecebimentoService service;
    private final OrdemServicoService ordemServicoService;
    private final ClienteService clienteService;
    
    @RequestMapping("/")
    public ModelAndView index() {
        var mv = new ModelAndView("home");
        mv.addObject("recebimentos", service.contadorRecebimentos());
        mv.addObject("clientes", clienteService.contadorClientes());
        mv.addObject("ordensServico", ordemServicoService.contadorOS());
        return mv;
    }

    @RequestMapping("/home")
    public String home() {
        return "redirect:/";
    }
}
