package com.myProject.demo.Conrollers;

import com.myProject.demo.Errors.ErrorResponse;
import com.myProject.demo.Exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<?> handleAssetNotFoundException(AssetNotFoundException e, HttpServletRequest request) {
        log.warn("Resource not found: {} - path={}", e.getMessage(), request.getRequestURI());
        ErrorResponse assetnotfound = new ErrorResponse(404, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(assetnotfound, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<?> handleportfolioNotFoundException(PortfolioNotFoundException e, HttpServletRequest request) {
        log.warn("Resource not found: {} - path={}", e.getMessage(), request.getRequestURI());
        ErrorResponse portfolionotfound = new ErrorResponse(404, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(portfolionotfound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<?> handleinsufficientFundexception(InsufficientFundsException e, HttpServletRequest request) {

        log.warn("Insufficient Funds: {} - path={}", e.getMessage(), request.getRequestURI());
        ErrorResponse insufficientfund = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(insufficientfund, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTradeException.class)
    public ResponseEntity<?> handleInvalidTradeException(InvalidTradeException ex, HttpServletRequest request) {

        log.warn("Invalid trade : {} - path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse invalidtrade = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(invalidtrade, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleuserNotfoundException(UserNotFoundException e, HttpServletRequest request)
    {
        log.warn("Resource not found: {} - path={}", e.getMessage(), request.getRequestURI());
        ErrorResponse usernotfound = new ErrorResponse(401, e.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(usernotfound, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidTradeQuantityException.class)
    public ResponseEntity<?> handleInvalidTradeQuantityException(InvalidTradeQuantityException ex, HttpServletRequest request)
    {
        log.warn("Invalid trade quantity: {} - path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse invalidtrade = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI()
        );
        return new ResponseEntity<>(invalidtrade, HttpStatus.BAD_REQUEST);
    }

}

