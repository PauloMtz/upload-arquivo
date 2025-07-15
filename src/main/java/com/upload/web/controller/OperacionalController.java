package com.upload.web.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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

import com.upload.domain.exception.EntidadeNaoEncontradaException;
import com.upload.domain.exception.ValidacaoException;
import com.upload.domain.model.Equipamento;
import com.upload.domain.model.OrdemServico;
import com.upload.domain.model.Recebimento;
import com.upload.domain.model.enums.Status;
import com.upload.domain.repository.EquipamentoRepository;
import com.upload.domain.repository.RecebimentoRepository;
import com.upload.domain.service.ClienteService;
import com.upload.domain.service.OrdemServicoService;
import com.upload.domain.service.RecebimentoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/operacao")
public class OperacionalController {
    
    private final RecebimentoService recebimentoService;
    private final ClienteService clienteService;
    private final OrdemServicoService ordemServicoService;
    private final EquipamentoRepository equipamentoRepository;
    private final RecebimentoRepository recebimentoRepository;

    @RequestMapping("/equipamentos-recebidos")
    public ModelAndView index() {
        var mv = new ModelAndView("operacoes/recebimentos");
        mv.addObject("recebimentos", recebimentoService.lista());
        return mv;
    }

    @GetMapping("/receber")
    public ModelAndView cadastrar() {
        var mv = new ModelAndView("operacoes/recebimento");
        mv.addObject("clientes", clienteService.lista());
        mv.addObject("recebimento", new Recebimento());
        return mv;
    }

    @PostMapping("/receber")
    public String cadastrar(@Valid Recebimento recebimento, BindingResult result,
            Model model, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "operacoes/recebimento";
        }

        Recebimento recEquip = recebimentoService.buscarEquipamento(recebimento.getEquipamento());

        if ((recEquip != null && recEquip.getStatus() != null && recEquip.getStatus() != Status.DEVOLVE_EQUIPAMENTO)) {
            model.addAttribute("error", "Esse equipamento já deu entrada para manutenção");
            return "operacoes/recebimento";
        }

        //recebimento.setStatus(Status.RECEBE_EQUIPAMENTO);
        //recebimento.setDataRecebimento(LocalDateTime.now());

        // altera o status e define a data de recebimento
        recebimento.receberEquipamento();
        recebimentoService.salvar(recebimento);
        attr.addFlashAttribute("success", "Recebimento efetuado com sucesso");
        return "redirect:/";
    }

    @GetMapping("/ordem-servico/{id}")
    public String criarOs(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Optional<Recebimento> recebimento = recebimentoRepository.findById(id);
        Equipamento equipamento = equipamentoRepository.findByNumSerie(recebimento.get().getEquipamento().getNumSerie());

        if (!recebimento.isPresent()) {
            attr.addFlashAttribute("error", "Equipamento não encontrado");
            return "redirect:/";
        }

        if (recebimento.get().getStatus() == Status.ABRE_ORDEM_SERVICO) {
            attr.addFlashAttribute("error", "Já existe OS aberta para este equipamento");
            return "redirect:/";
        }

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setEquipamento(equipamento);
        model.addAttribute("ordemServico", ordemServico);
        return "operacoes/abertura-os";
    }

    @PostMapping("/ordem-servico/{id}")
    public String criarOs(@Valid OrdemServico ordemServico, BindingResult result,
    Model model, RedirectAttributes attr, @PathVariable("id") Long id) {

        if (result.hasErrors()) {
            return "operacoes/abertura-os";
        }

        try {
            Recebimento recebimento = recebimentoRepository.findById(id).orElseThrow();
            Equipamento equipamento = equipamentoRepository.findByNumSerie(recebimento.getEquipamento().getNumSerie());

            // gera um número de OS com data invertida e id
            LocalDateTime data = LocalDateTime.now();
            String dataFormatadaOs = data.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            var numeroOs = dataFormatadaOs + String.format("%03d", recebimento.getId());

            OrdemServico os = new OrdemServico();
            os.setEquipamento(equipamento);
            os.setRecebimento(recebimento);
            os.setDescricao(ordemServico.getDescricao());
            os.setValor(ordemServico.getValor());
            //os.setDataOrdemServico(LocalDateTime.now());
            os.setNumeroOs(numeroOs);
            //os.setStatus(Status.OS_ABERTA);
            //recebimento.setStatus(Status.ABRE_ORDEM_SERVICO);
            
            os.abreOrdemServico();
            recebimento.abreOrdemServico();
            ordemServicoService.salvar(os);
            return "redirect:/";
        } catch(ValidacaoException e) {
            result.addError(e.getFieldError());
            return "operacoes/abertura-os";
        }
    }

    @GetMapping("/detalhes/{id}")
    public ResponseEntity<?> detalhes(@RequestParam("id") Long id, RedirectAttributes attr) {
        Optional<Recebimento> recebimento = recebimentoRepository.findById(id);
        
        if (!recebimento.isPresent()) {
            throw new EntidadeNaoEncontradaException("\nOperacionalController - detalhes");
        }

        return ResponseEntity.ok(recebimento);
    }
}
