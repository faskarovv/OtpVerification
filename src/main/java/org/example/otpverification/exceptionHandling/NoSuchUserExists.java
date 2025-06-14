package org.example.otpverification.exceptionHandling;

public class NoSuchUserExists extends RuntimeException{

    public  NoSuchUserExists(String message){
        super(message);
    }
}
