package com.example.doctorcare.infrastructure.security.old.auth.security.common;

public interface CountErrorPassowrdCommand {
    /**
     * @param password To check password is valid or not
     * @return int is the number of errors
     */
    int passworRegisterdErrors(String password);
}
