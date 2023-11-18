package learn.chronicles.controllers;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalErrHandler {

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        //this might happen from a JSON parsing error
        return new ResponseEntity<>(List.of("Check yourself(JSON) before you wreck yourself"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageConversionException(HttpMessageNotReadableException ex) {
        //this might happen from a JSON parsing error
        return new ResponseEntity<>(List.of("Check yourself(JSON) before you wreck yourself"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(TypeMismatchException ex) {
        //bad type in parameter
        return new ResponseEntity<>(List.of("Something's wrong with the G-Diffuser."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(List.of("These are not the droids you are looking for..."), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKey(Exception ex) {
        return new ResponseEntity<>(List.of("Already exists!"), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return new ResponseEntity<>(List.of("Something went wrong on our end :("), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
