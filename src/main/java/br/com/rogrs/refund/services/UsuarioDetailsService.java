package br.com.rogrs.refund.services;

import br.com.rogrs.refund.entity.Usuario;
import br.com.rogrs.refund.repository.UsuarioRepository;
import br.com.rogrs.refund.security.UsuarioDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    public UsuarioDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = repository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado: " + username
                        )
                );

        return new UsuarioDetails(usuario);
    }
}
