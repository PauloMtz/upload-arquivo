package com.upload.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upload.domain.exception.ArquivoException;

@Service
public class UploadService {
    
    private final Path pastaUpload = Paths.get("uploads");

    public String gerarNomeArquivo(String nomeArquivo) {
        
        int lastIndex = nomeArquivo.lastIndexOf(".");
        String ext = nomeArquivo.substring(lastIndex);

        return UUID.randomUUID().toString().replace("-", "")
            + LocalDateTime.now().toString().replaceAll("[\\:\\-\\.]", "") 
            + ext;
    }

    public String gravarLocal(MultipartFile arquivo) throws IOException {

        // se a pasta não existir, será criada
        if(!Files.exists(pastaUpload)) {
            Files.createDirectories(pastaUpload);
        }

        // antes de salvar, cria um nome para ele
        String nomeArquivo = gerarNomeArquivo(arquivo.getOriginalFilename());

        // grava o arquivo na pasta
        Files.copy(arquivo.getInputStream(), pastaUpload.resolve(nomeArquivo));

        // retorna o nome do arquivo criado
        return nomeArquivo;
    }

    public void remover(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            Files.deleteIfExists(arquivoPath);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao remover arquivo", e);
        }
    }

    public InputStream recuperar(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            return Files.newInputStream(arquivoPath);
        } catch (Exception e) {
            throw new ArquivoException("Erro ao recuperar arquivo.", e);
        }
    }

    public void substituirArquivo(String arquivoArmazenado, 
        MultipartFile novoArquivo) throws IOException {

        this.gravarLocal(novoArquivo);

        // antes de armazenar em disco, remover a anterior
        if (arquivoArmazenado != null) {
            this.remover(arquivoArmazenado);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return pastaUpload.resolve(Path.of(nomeArquivo));
    }
}
