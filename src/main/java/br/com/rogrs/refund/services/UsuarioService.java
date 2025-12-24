package br.com.rogrs.refund.services;

import br.com.rogrs.refund.entity.Usuario;
import br.com.rogrs.refund.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void salvar(Usuario usuario, String passwordPlain) {

        if (passwordPlain != null && !passwordPlain.isBlank()) {
            usuario.setPassword(encoder.encode(passwordPlain));
        }
        usuario.setActivated(true);
        repository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado com id: " + id
                        )
                );
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado")
                );
    }

}
