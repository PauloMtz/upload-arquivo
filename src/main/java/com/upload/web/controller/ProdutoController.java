package com.upload.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

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
    private ProdutoRepository produtoRepository;

    @Autowired
    private UploadService uploadService;

    @GetMapping("/listar")
    public ModelAndView listar() {
        var mv = new ModelAndView("produtos/lista"); // template
        mv.addObject("produtos", produtoRepository.findAll());
        return mv;
    }

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
            return "produtos/form";
        }

        // verifica se já tem um produto cadastrado com mesmo nome
        Produto nomeProduto = produtoService.buscarPorNome(produto.getNome());

        if (nomeProduto != null) {
            attr.addFlashAttribute("error", "Já existe um produto com o mesmo nome");
            return "redirect:/produto/cadastrar";
        }

        // antes de salvar objeto, gravar nome foto
        try {
            var arquivoNome = uploadService.gravarLocal(arquivo);
            produto.setFoto(arquivoNome);

            produto.setDataCriacao(LocalDateTime.now());
            produto.setAtivo(true);
            produto.setSaldo(0);

            produtoService.salvar(produto);
            attr.addFlashAttribute("success", "Cadastro efetuado com sucesso.");
            return "redirect:/produto/listar";
        } catch (Exception e) {
            // se não conseguir salvar os dados, remove o arquivo
            uploadService.remover(produto.getFoto());
            attr.addFlashAttribute("error", e.getMessage());
            return "redirect:/produto/cadastrar";
        }
    }

    @GetMapping("/buscar-produto")
    public ModelAndView buscarProduto(@RequestParam("codigo") String codigo) {
        var mv = new ModelAndView("produtos/lista");
        mv.addObject("produtos", produtoRepository.findByCodigo(codigo));
        return mv;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        var mv = new ModelAndView("produtos/form");
        mv.addObject("produto", produtoRepository.findById(id));
        return mv;
    }

    @PostMapping("/editar/{id}")
    public String editar(@RequestParam("imagem") MultipartFile arquivo, 
        @Valid Produto produto, BindingResult result, @PathVariable Long id,
        RedirectAttributes attr) throws IOException {
        
        if (result.hasErrors()) {
            return "produtos/form";
        }

        // -------- tratamento do upload de imagem --------
        var produtoAtual = produtoRepository.findById(id);

        // verifica se já tem um produto cadastrado com mesmo nome
        Produto nomeProduto = produtoService.buscarPorNome(produto.getNome());

        if (nomeProduto != null && nomeProduto.getId() != id) {
            attr.addFlashAttribute("error", "Já existe um produto com o mesmo nome");
            return "redirect:/produto/cadastrar";
        }

        // verifica alguns dados existentes
        if (arquivo.isEmpty()) {
            produto.setFoto(produtoAtual.get().getFoto());
            // para não zerar o saldo na hora da atualização
            produto.setSaldo(produtoAtual.get().getSaldo());
            // para não alterar para inativo, caso esteja ativo
            produto.setAtivo(produto.isAtivo());
        } else {
            // remove a imagem atual
            uploadService.remover(produtoAtual.get().getFoto());
            // grava a nova
            var arquivoNome = uploadService.gravarLocal(arquivo);
            // para não zerar o saldo na hora da atualização
            produto.setSaldo(produtoAtual.get().getSaldo());
            // para não alterar para inativo, caso esteja ativo
            produto.setAtivo(produtoAtual.get().isAtivo());
            produto.setFoto(arquivoNome);
        }
        // ----- fim tratamento de uploade de imagem ------

        produto.setDataAtualizacao(LocalDateTime.now());
        produtoService.salvar(produto);
        attr.addFlashAttribute("success", "Item atualizado com sucesso.");
        return "redirect:/produto/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attr) {
        try {
            Produto produto = produtoService.buscar(id);
            produtoService.excluir(id);
            uploadService.remover(produto.getFoto());
            attr.addFlashAttribute("success", "Item excluído com sucesso.");
            return "redirect:/produto/listar";
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Esse produto está associado a algum recebimento e não pode ser excluído");
            return "redirect:/produto/listar";
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
