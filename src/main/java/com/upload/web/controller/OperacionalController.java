package com.upload.web.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/receber")
    public ModelAndView cadastrar() {
        var mv = new ModelAndView("operacoes/recebimento"); // template
        mv.addObject("clientes", clienteService.lista());
        mv.addObject("recebimento", new Recebimento());
        return mv;
    }

    @PostMapping("/receber")
    public String cadastrar(@Valid Recebimento recebimento, BindingResult result,
            Model model, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "operacoes/recebimento"; // template
        }

        Recebimento recEquip = recebimentoService.buscarEquipamento(recebimento.getEquipamento());

        if ((recEquip != null && recEquip.getStatus() != null && recEquip.getStatus() != Status.DEVOLVE_EQUIPAMENTO)) {
            model.addAttribute("error", "Esse equipamento já deu entrada para manutenção");
            return "operacoes/recebimento"; // template
        }

        //System.out.println("\n>>> Dados do recebimento: " + recebimento);

        //recebimento.setStatus(Status.RECEBE_EQUIPAMENTO);
        //recebimento.setDataRecebimento(LocalDateTime.now());

        // altera o status e define a data de recebimento
        recebimento.receberEquipamento();
        recebimentoService.salvar(recebimento);
        attr.addFlashAttribute("success", "Recebimento efetuado com sucesso");
        return "redirect:/"; // rota
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
            return "redirect:/"; // template
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
            //System.out.println("\n>>> [OperacionalControler] Result Error" + result.toString());
            return "operacoes/abertura-os"; // template
        }

        Recebimento recebimento = recebimentoRepository.findById(id).orElseThrow();
        Equipamento equipamento = equipamentoRepository.findByNumSerie(recebimento.getEquipamento().getNumSerie());

        // gera um número de OS com data invertida e id
        LocalDateTime data = LocalDateTime.now();
		String dataFormatadaOs = data.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        var numeroOs = dataFormatadaOs + String.format("%03d", recebimento.getId());

        ordemServico.setEquipamento(equipamento);
        ordemServico.setRecebimento(recebimento);
        //ordemServico.setDataOrdemServico(LocalDateTime.now());
        ordemServico.setNumeroOs(numeroOs);
        //ordemServico.setStatus(Status.OS_ABERTA);
        //recebimento.setStatus(Status.ABRE_ORDEM_SERVICO);
        //System.out.println("\n>>> [OperacionalController] Dados da OS: " + ordemServico);
        ordemServico.abreOrdemServico();
        recebimento.abreOrdemServico();
        ordemServicoService.salvar(ordemServico);
        return "redirect:/";
    }
}
