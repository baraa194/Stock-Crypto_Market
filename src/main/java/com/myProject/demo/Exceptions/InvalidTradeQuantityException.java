package com.myProject.demo.Exceptions;

public class InvalidTradeQuantityException extends RuntimeException{
    public InvalidTradeQuantityException(String msg){
        super(msg);
    }
}
