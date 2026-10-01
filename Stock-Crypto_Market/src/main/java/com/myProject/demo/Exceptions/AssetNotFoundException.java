package com.myProject.demo.Exceptions;

public class AssetNotFoundException extends RuntimeException{
    public AssetNotFoundException(String msg) {
        super(msg);
    }
}
