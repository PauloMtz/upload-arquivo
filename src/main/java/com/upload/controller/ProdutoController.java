package com.upload.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.upload.domain.model.Produto;
import com.upload.domain.repository.ProdutoRepository;
import com.upload.domain.service.ProdutoService;
import com.upload.domain.service.UploadService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private static String caminhoImagens = "uploads/";
    
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @RequestMapping("/cadastrar")
    public ModelAndView carregaCadastro() {
        var mv = new ModelAndView("produtos/form");
        mv.addObject("produto", new Produto());
        return mv;
    }

    @PostMapping(path = "/cadastrar")
    public String cadastrar(@Valid Produto produto, BindingResult result,
        @RequestParam("imagem") MultipartFile arquivo,
        RedirectAttributes attr) throws Exception {

        if (result.hasErrors()) {
            return "materiais/form";
        }

        // antes de salvar objeto, gravar nome foto
        var arquivoNome = uploadService.gravar(arquivo);
        produto.setFoto(arquivoNome);

        produtoService.salvar(produto);
        
        return "redirect:/produto/listar";
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        var mv = new ModelAndView("produtos/lista"); // template
        mv.addObject("produtos", produtoRepository.findAll());
        return mv;
    }

    @GetMapping("/mostrarImagem/{imagem}")
	@ResponseBody
	public byte[] retornarImagem(@PathVariable("imagem") String imagem) throws IOException {

		File imagemArquivo = new File(caminhoImagens + imagem);

		if (imagem != null || imagem.trim().length() > 0) {
			return Files.readAllBytes(imagemArquivo.toPath());
		}

		return null;
	}

    @GetMapping("/editar/{id}")
    public ModelAndView carregarProduto(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("produtos/form");
        mv.addObject("produto", produtoRepository.findById(id));
        return mv;
    }

    @PostMapping("/editar/{id}")
    public String editarProduto(@RequestParam("imagem") MultipartFile arquivo, @PathVariable Long id, 
        @Valid Produto produto, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            return "produtos/form";
        }

        // -------- tratamento do upload de imagem --------
        var produtoAtual = produtoService.buscarPorId(id);

        // verifica se tem uma foto no cadastro
        if (arquivo.isEmpty()) {
            produto.setFoto(produtoAtual.getFoto());
        } else {
            var filename = uploadService.gravar(arquivo);
            produto.setFoto(filename);
        }
        // ----- fim tratamento de uploade de imagem ------

        produtoService.salvar(produto);
        return "redirect:/produto/listar";
    }

    @GetMapping("/excluir/{id}")
    public String removerArquivo(@PathVariable Long id) {
        produtoService.excluir(id);
        return "redirect:/produto/listar";
    }
}
