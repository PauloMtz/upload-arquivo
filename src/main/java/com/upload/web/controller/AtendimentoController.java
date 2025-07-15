package com.upload.web.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.upload.domain.exception.ArquivoNaoEncontradoException;
import com.upload.domain.model.Atendimento;
import com.upload.domain.model.OrdemServico;
import com.upload.domain.model.Produto;
import com.upload.domain.model.ProdutoAtendimento;
import com.upload.domain.model.ProdutoAtendimentoId;
import com.upload.domain.service.AtendimentoService;
import com.upload.domain.service.OrdemServicoService;
import com.upload.domain.service.ProdutoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/atendimento")
public class AtendimentoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private AtendimentoService atendimentoService;

    @RequestMapping("/listar")
    public ModelAndView index() {
        var mv = new ModelAndView("atendimentos/lista");
        mv.addObject("atendimentos", atendimentoService.lista());
        return mv;
    }
    
    @GetMapping("/ordem-servico/{numeroOs}")
    public String atenderOs(@PathVariable String numeroOs, Model model, RedirectAttributes attr) {
        OrdemServico registroEncontrado = ordemServicoService.buscarOrdemServico(numeroOs);

        if (registroEncontrado == null) {
            attr.addFlashAttribute("error", "Registro não encontrado");
            return "redirect:/";
        }
        
        Atendimento atendimento = new Atendimento();
        atendimento.setOrdemServico(registroEncontrado);

        model.addAttribute("atendimento", atendimento);
        return "atendimentos/atender-os";
    }

    // método sugerido pelo chatGPT
    @PostMapping("/ordem-servico/{numeroOs}")
    public String atenderOs(@Valid Atendimento atendimento, BindingResult result,
        @RequestParam Map<String, String> requestParams, Model model, RedirectAttributes attr,
        @PathVariable String numeroOs) throws ArquivoNaoEncontradoException {

        if (result.hasErrors()) {
            return "atendimentos/atender-os";
        }

        OrdemServico ordemServico = ordemServicoService.buscarOrdemServico(numeroOs);
        if (ordemServico == null) {
            attr.addFlashAttribute("error", "Ordem de serviço não encontrada.");
            return "redirect:/";
        }

        atendimento.setOrdemServico(ordemServico);
        atendimento.setDataAtendimento(LocalDateTime.now());

        List<ProdutoAtendimento> produtoAtendimentos = new ArrayList<>();

        try {
            int index = 0;
            while (true) {
                String codigo = requestParams.get("produtos[" + index + "].codigo");
                String qtdStr = requestParams.get("produtos[" + index + "].quantidade");

                if (codigo == null || qtdStr == null) break;

                int quantidade = Integer.parseInt(qtdStr);
                Produto produto = produtoService.buscarProduto(codigo);

                produtoService.baixarSaldo(codigo, quantidade);

                ProdutoAtendimento pa = new ProdutoAtendimento();
                pa.setAtendimento(atendimento);
                pa.setProduto(produto);
                pa.setQuantidade(quantidade);
                pa.setId(new ProdutoAtendimentoId(null, produto.getId()));

                produtoAtendimentos.add(pa);
                index++;
            }

            atendimento.setProdutos(produtoAtendimentos);
            produtoAtendimentos.forEach(pa -> pa.setAtendimento(atendimento));
            atendimento.calcularValorTotal();

            atendimentoService.salvar(atendimento);

            ordemServico.atendeOrdemServico();
            ordemServicoService.atualizaStatusRecebimento(numeroOs);

        } catch (Exception e) {
            attr.addFlashAttribute("error", "Erro ao processar produtos: " + e.getMessage());
            return "redirect:/atendimento/ordem-servico/" + numeroOs;
        }

        attr.addFlashAttribute("success", "Atendimento salvo com sucesso.");
        return "redirect:/atendimento/ordem-servico/" + numeroOs;
    }
}
