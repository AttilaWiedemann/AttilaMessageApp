package edu.progmatic.messageapp.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleErrors(Model model, Exception ex){
        String trace = "";
        for(StackTraceElement x :ex.getStackTrace()) {
            trace += x.toString();
            trace += "\n";
        }
        model.addAttribute("exceptionMessage", trace);
        return "myError";
    }

}
