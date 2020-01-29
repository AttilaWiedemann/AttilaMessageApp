package edu.progmatic.messageapp.modell;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(reason = "No such message", code = HttpStatus.NOT_FOUND)
public class NoSuchMessageException extends RuntimeException {
}
