package com.myProject.demo.Exceptions;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String msg) {
        super(msg);
    }
}
