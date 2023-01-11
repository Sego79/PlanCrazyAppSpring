package com.plancrazyappfrontofficespring.model.exception;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String email) {
        super(email + " already exist in database");
    }
}
