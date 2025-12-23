package br.com.rogrs.refund.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ArquivoService {

    private static final String DIRETORIO_NOTAS = "uploads/notas-fiscais";

    public String salvarArquivoNotaFiscal(MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de nota fiscal inválido");
        }

        try {
            // Cria diretório se não existir
            Path diretorio = Paths.get(DIRETORIO_NOTAS);
            Files.createDirectories(diretorio);

            // Nome original
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = extrairExtensao(nomeOriginal);

            // Nome seguro e único
            String nomeArquivo = UUID.randomUUID() + extensao;

            // Caminho final
            Path destino = diretorio.resolve(nomeArquivo);

            // Copia o arquivo
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return nomeArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar nota fiscal", e);
        }
    }

    private String extrairExtensao(String nome) {
        if (nome == null || !nome.contains(".")) {
            return "";
        }
        return nome.substring(nome.lastIndexOf(".")).toLowerCase();
    }
}
