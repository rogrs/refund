package br.com.rogrs.refund.services;

import br.com.rogrs.refund.entity.Reembolso;
import br.com.rogrs.refund.repository.ReembolsoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReembolsoService {
    private ArquivoService arquivoService;


    private final ReembolsoRepository repository;

    public ReembolsoService(ReembolsoRepository repository, ArquivoService arquivoService) {
        this.repository = repository;
        this.arquivoService =arquivoService;
    }

    public List<Reembolso> listarByNumeroSR(String numeroSr) {

        if (numeroSr == null || numeroSr.isBlank()) {
            return listar();
        }

        return repository.findByNumeroSrContainingIgnoreCase(numeroSr.trim());
    }

    public List<Reembolso> listar() {
        return repository.findAll();
    }

    public Reembolso salvar(Reembolso reembolso) {

        return repository.save(reembolso);
    }

    public Reembolso buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public String salvarArquivoNotaFiscal(MultipartFile arquivo) {

       return arquivoService.salvarArquivoNotaFiscal(arquivo);
    }

    public String salvarArquivoComprovante(MultipartFile arquivo) {

        return arquivoService.salvarArquivoNotaFiscal(arquivo);
    }
}
