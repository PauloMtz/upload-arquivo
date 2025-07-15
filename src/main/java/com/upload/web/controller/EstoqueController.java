package com.upload.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.upload.domain.model.Entrada;
import com.upload.domain.model.Produto;
import com.upload.domain.service.EntradaService;
import com.upload.domain.service.ProdutoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private static String caminhoImagens = "uploads/";
    
    @Autowired
    private EntradaService entradaService;

    @Autowired
    private ProdutoService produtoService;

    @RequestMapping("/receber")
    public ModelAndView carregaCadastro() {
        var mv = new ModelAndView("estoque/entrada");
        mv.addObject("entrada", new Entrada());
        return mv;
    }

    @PostMapping(path = "/receber")
    public String cadastrar(@Valid Entrada entrada, BindingResult result, Produto produto,
        RedirectAttributes attr) {
            
        if (result.hasErrors()) {
            return "estoque/entrada";
        }
        
        try {
            produto = produtoService.buscar(produto.getId());
            entrada.setDataAtualizacao(LocalDateTime.now());
            entrada.setProduto(produto);
            produto.incrementaSaldo(Integer.valueOf(entrada.getQuantidade()));
            produto.setDataAtualizacao(entrada.getDataAtualizacao());
            entradaService.salvar(entrada);
            attr.addFlashAttribute("success", "Cadastro efetuado com sucesso.");
            return "redirect:/produto/listar";
        } catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
            return "redirect:/estoque/receber";
        }
    }

    @GetMapping("/buscar-produto")
    public ResponseEntity<?> buscarProduto(RedirectAttributes attr, 
        @RequestParam("busca") String codigo) throws Exception {

        try {
            Produto produto = produtoService.buscarProduto(codigo);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @SuppressWarnings("null")
    @GetMapping("/mostrarImagem/{imagem}")
	@ResponseBody
	public byte[] retornarImagem(@PathVariable("imagem") String imagem) throws IOException {

		File imagemArquivo = new File(caminhoImagens + imagem);

		if (imagem != null || imagem.trim().length() > 0) {
			return Files.readAllBytes(imagemArquivo.toPath());
		}

		return null;
	}
}