package org.example.miniprojectspring.exception;

public class OTPExpiredException extends Exception{
    public OTPExpiredException(String message) {
        super(message);
    }
}
