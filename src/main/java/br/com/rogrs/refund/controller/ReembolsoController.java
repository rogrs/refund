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


    /* ==========================================================
       HELPERS
       ========================================================== */

    private void popularCombos(Model model) {
        model.addAttribute("pacientes", Paciente.values());
        model.addAttribute("tipos", TipoTerapia.values());
    }
}
