package com.plancrazyappfrontofficespring.exception;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String email) {
        super(email + " already exist in database");
    }
}
