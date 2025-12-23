package br.com.rogrs.refund.controller;

import br.com.rogrs.refund.entity.Paciente;
import br.com.rogrs.refund.entity.Reembolso;
import br.com.rogrs.refund.repository.ReembolsoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ReembolsoRepository repository;

    public DashboardController(ReembolsoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String dashboard(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            Model model
    ) {
        YearMonth periodo = YearMonth.of(
                ano != null ? ano : LocalDate.now().getYear(),
                mes != null ? mes : LocalDate.now().getMonthValue()
        );

        LocalDate inicio = periodo.atDay(1);
        LocalDate fim = periodo.atEndOfMonth();

        List<Reembolso> reembolsos =
                repository.findByDataPedidoBetween(inicio, fim);

        BigDecimal totalNF = BigDecimal.ZERO;
        BigDecimal totalReembolso = BigDecimal.ZERO;

        Map<Paciente, BigDecimal> totalPorPaciente = new EnumMap<>(Paciente.class);

        for (Paciente p : Paciente.values()) {
            totalPorPaciente.put(p, BigDecimal.ZERO);
        }

        for (Reembolso r : reembolsos) {
            if (r.getValorNotaFiscal() != null)
                totalNF = totalNF.add(r.getValorNotaFiscal());

            if (r.getValorReembolso() != null)
                totalReembolso = totalReembolso.add(r.getValorReembolso());

            if (r.getValorReembolso() != null)
                totalPorPaciente.put(
                        r.getPaciente(),
                        totalPorPaciente.get(r.getPaciente()).add(r.getValorReembolso())
                );
        }

        model.addAttribute("totalNF", totalNF);
        model.addAttribute("totalReembolso", totalReembolso);
        model.addAttribute("labels", totalPorPaciente.keySet());
        model.addAttribute("values", totalPorPaciente.values());
        model.addAttribute("periodo", periodo);

        return "dashboard/index";
    }

}
