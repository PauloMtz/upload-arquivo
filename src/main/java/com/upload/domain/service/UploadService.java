package com.upload.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upload.exception.ArquivoException;
import com.upload.helper.NomeArquivoHelper;

@Service
public class UploadService {
    
    private final Path pastaUpload = Paths.get("uploads");

    // método para gravar o arquivo
    public String gravar(MultipartFile arquivo) throws Exception {

        // se a pasta não existir, será criada
        if(!Files.exists(pastaUpload)) {
            Files.createDirectories(pastaUpload);
        }

        // antes de salvar, cria um nome para ele
        String nomeArquivo = NomeArquivoHelper.gerarNomeArquivo(arquivo.getOriginalFilename());

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

    private Path getArquivoPath(String nomeArquivo) {
        return pastaUpload.resolve(Path.of(nomeArquivo));
    }
}
