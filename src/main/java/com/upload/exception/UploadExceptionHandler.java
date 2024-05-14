package com.upload.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UploadExceptionHandler {
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleError(MaxUploadSizeExceededException e, 
        RedirectAttributes attr) {
        
        attr.addFlashAttribute("error", 
            "Arquivo maior que o tamanho máximo permitido");
        
        return "redirect:/produto/cadastrar";
    }
}
