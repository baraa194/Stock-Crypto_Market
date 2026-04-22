package com.myProject.demo.Exceptions;

public class InvalidTradeException extends RuntimeException{
    public InvalidTradeException(String msg) {
       super(msg);
    }
}
