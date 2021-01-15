package com.techelevator.tenmo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TransferServiceException extends Exception {
    private static final long serialVersionUID = 1L;
}
