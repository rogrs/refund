package br.com.rogrs.refund.controller;

import br.com.rogrs.refund.entity.Paciente;
import br.com.rogrs.refund.entity.Reembolso;
import br.com.rogrs.refund.entity.TipoTerapia;
import br.com.rogrs.refund.services.ReembolsoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@RequestMapping("/reembolsos")
public class ReembolsoController {

    private final ReembolsoService service;

    public ReembolsoController(ReembolsoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String numeroSr, Model model) {
        model.addAttribute("reembolsos", service.listarByNumeroSR(numeroSr));
        model.addAttribute("numeroSr", numeroSr);
        return "reembolso/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("reembolso", new Reembolso());
        model.addAttribute("pacientes", Paciente.values());
        model.addAttribute("tipos", TipoTerapia.values());
        return "reembolso/form";
    }

    @PostMapping
    public String salvar(
            @Valid @ModelAttribute Reembolso reembolso,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", Paciente.values());
            model.addAttribute("tipos", TipoTerapia.values());
            return "reembolso/form";
        }
        service.salvar(reembolso);
        return "redirect:/reembolsos";
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("reembolso", service.buscarPorId(id));
        model.addAttribute("pacientes", Paciente.values());
        model.addAttribute("tipos", TipoTerapia.values());
        return "reembolso/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/reembolsos";
    }

    @GetMapping("/novo-wizard")
    public String novoWizard(Model model) {
        model.addAttribute("reembolso", new Reembolso());
        popularCombos(model);
        return "reembolso/wizard";
    }

    @PostMapping("/wizard/passo1")
    public String passo1(
            @Valid @ModelAttribute("reembolso") Reembolso reembolso,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            popularCombos(model);
            model.addAttribute("abaAtiva", "passo1");
            return "reembolso/wizard";
        }

        reembolso.setDataPedido(LocalDate.now());
        Reembolso salvo = service.salvar(reembolso);

        model.addAttribute("reembolso", salvo);
        model.addAttribute("abaAtiva", "passo2");
        popularCombos(model);

        return "reembolso/wizard";
    }

    @PostMapping("/wizard/passo2")
    public String passo2(
            @RequestParam Long id,
            @RequestParam("notaFiscal") MultipartFile arquivo,
            Model model
    ) {
        Reembolso r = service.buscarPorId(id);

        if (!arquivo.isEmpty()) {
            r.setArquivoNotaFiscal(service.salvarArquivoNotaFiscal(arquivo));
            service.salvar(r);
        }

        model.addAttribute("reembolso", r);
        model.addAttribute("abaAtiva", "passo3");
        popularCombos(model);

        return "reembolso/wizard";
    }


    /* ==========================================================
       HELPERS
       ========================================================== */

    private void popularCombos(Model model) {
        model.addAttribute("pacientes", Paciente.values());
        model.addAttribute("tipos", TipoTerapia.values());
    }
}
