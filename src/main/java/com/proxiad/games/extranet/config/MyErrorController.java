package com.proxiad.games.extranet.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class MyErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            Integer statusCode = Integer.valueOf(status.toString());
            String reasonPhrase = HttpStatus.valueOf(statusCode).getReasonPhrase();

            return String.format("<h1>%d - %s on %s</h1>", statusCode, reasonPhrase, uri);
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}