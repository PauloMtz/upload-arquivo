package com.upload.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.upload.domain.service.RecebimentoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class HomeController {

    private final RecebimentoService service;
    
    @RequestMapping("/")
    public ModelAndView index() {
        var mv = new ModelAndView("home"); // template
        mv.addObject("recebimentos", service.lista());
        //System.out.println("\n>>> [HomeController] Lista: " + service.lista());
        return mv;
    }

    @RequestMapping("/home")
    public String home() {
        return "redirect:/"; // rota
    }
}
