package com.example.doctorcare.auth.security.common;

public interface CommonService {
    /**
     * @param password To check password is valid or not
     * @return int is the number of errors
     */

    int passworRegisterdErrors(String password);
}
