package br.com.rogrs.refund.controller;

import br.com.rogrs.refund.entity.Usuario;
import br.com.rogrs.refund.security.UsuarioDetails;
import br.com.rogrs.refund.services.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    /* =====================================================
       CRIAR USUÁRIO
       ===================================================== */

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("modo", "novo");
        return "usuario/form";
    }

    /* =====================================================
       EDITAR USUÁRIO (SEM ID)
       ===================================================== */

    @GetMapping("/editar")
    public String editar(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        Usuario usuario = service.buscarPorEmail(userDetails.getUsername());
        model.addAttribute("usuario", usuario);
        model.addAttribute("modo", "editar");
        return "usuario/form";
    }

    /* =====================================================
       PERFIL DO USUÁRIO LOGADO
       ===================================================== */

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UsuarioDetails userDetails,
                         Model model) {

        model.addAttribute("usuario", userDetails.getUsuario());
        return "usuario/perfil";
    }


    /* =====================================================
       SALVAR (NOVO / EDITAR)
       ===================================================== */

    @PostMapping
    public String salvar(
            @ModelAttribute Usuario usuario,
            @RequestParam(required = false) String passwordPlain
    ) {
        service.salvar(usuario, passwordPlain);
        return "redirect:/home";
    }
}
