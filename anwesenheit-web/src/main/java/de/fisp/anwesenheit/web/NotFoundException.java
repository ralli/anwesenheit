package de.fisp.anwesenheit.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        // TODO Auto-generated constructor stub
    }
    
    public NotFoundException(String message) {
        super(message);
    }
}
