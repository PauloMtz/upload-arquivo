package com.upload.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.upload.domain.service.UploadService;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;
    
    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("upload/form-1");
        return mv;
    }

    @GetMapping("/cadastrar-varios")
    public ModelAndView cadastrar2() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("upload/form-2");
        return mv;
    }

    @PostMapping("/cadastrar")
    public String upload(@RequestParam("imagem") MultipartFile arquivo,
        ModelMap model) throws IOException {
        
        System.out.println("\n>>> Informações:\n");
        System.out.println("Nome: " + arquivo.getOriginalFilename() + "\n");
        System.out.println("Tamanho: " + arquivo.getSize() + "\n");
        System.out.println("Tipo: " + arquivo.getContentType() + "\n");

        File pastaLocal = new File(new ClassPathResource(".")
            .getFile().getPath() + "/static/uploads");

        if (!pastaLocal.exists()) {
            pastaLocal.mkdirs();
        }

        String nomeArquivo = uploadService.gerarNomeArquivo(arquivo.getOriginalFilename());
        Path path = Paths.get(pastaLocal.getAbsolutePath() + File.separator + nomeArquivo);
        Files.copy(arquivo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        model.put("foto", nomeArquivo);
        model.addAttribute("success", String.format("Nome do arquivo: %s", nomeArquivo));
        System.out.println("Nome final: " + nomeArquivo);

        return "upload/view";
    }

    @PostMapping("/cadastrar-varios")
    public String uploads2(@RequestParam("imagens") MultipartFile[] arquivos, ModelMap model) {
        
        List<String> nomesArquivos = new ArrayList<String>();

        for (MultipartFile arquivo : arquivos) {
            //System.out.println("Nome: " + arquivo.getOriginalFilename() + "\n");
            //System.out.println("Tamanho: " + arquivo.getSize() + "\n");
            //System.out.println("Tipo: " + arquivo.getContentType() + "\n");
            //System.out.println("----------------------\n");

            try {
                File pastaLocal = new File(new ClassPathResource(".")
                    .getFile().getPath() + "/static/uploads");
    
                if (!pastaLocal.exists()) {
                    pastaLocal.mkdirs();
                }
    
                String nomeArquivo = uploadService.gerarNomeArquivo(arquivo.getOriginalFilename());
                Path path = Paths.get(pastaLocal.getAbsolutePath() + File.separator + nomeArquivo);
                Files.copy(arquivo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                nomesArquivos.add(nomeArquivo);
                model.put("fotos", nomesArquivos);
                model.addAttribute("success", "Arquivos enviados com sucesso");
                System.out.println("Nome final: " + nomeArquivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "upload/view-2";
    }
}
