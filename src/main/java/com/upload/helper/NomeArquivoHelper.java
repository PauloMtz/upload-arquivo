package com.upload.helper;

import java.time.LocalDateTime;
import java.util.UUID;

public class NomeArquivoHelper {
    
    public static String gerarNomeArquivo(String nomeArquivo) {
        int lastIndex = nomeArquivo.lastIndexOf(".");
        String ext = nomeArquivo.substring(lastIndex);
        return UUID.randomUUID().toString().replace("-", "")
            + LocalDateTime.now().toString().replaceAll("[\\:\\-\\.]", "") 
            + ext;
    }
}
