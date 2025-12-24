package br.com.rogrs.refund.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
        model.addAttribute("erro", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandler(NoHandlerFoundException ex) {
        return "error/404";
    }
}
