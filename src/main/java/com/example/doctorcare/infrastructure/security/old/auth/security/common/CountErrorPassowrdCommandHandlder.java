package com.example.doctorcare.infrastructure.security.old.auth.security.common;

import com.example.doctorcare.infrastructure.common.utils.Const.PASSWORD;

import io.swagger.v3.oas.annotations.servers.Server;

@Server
public class CountErrorPassowrdCommandHandlder implements CountErrorPassowrdCommand{

   @Override
    public int passworRegisterdErrors(String password) {
        boolean isNumberInside = false;
        boolean isLowerCaseInside = false;
        boolean isUpperCaseInside = false;
        boolean isSpecialCharacters = false;
        int count = 0;

        for (Character ch : password.toCharArray()) {
            if (Character.isLowerCase(ch))
                isLowerCaseInside = true;
            else if (Character.isUpperCase(ch))
                isUpperCaseInside = true;
            else if (Character.isDigit(ch))
                isNumberInside = true;
            else if (PASSWORD.SPECIAL_CHACTERLIST.contains(ch))
                isSpecialCharacters = true;
        }

        if (!isNumberInside)
            count++;
        if (!isLowerCaseInside)
            count++;
        if (!isUpperCaseInside)
            count++;
        if (!isSpecialCharacters)
            count++;

        return Math.max(count, 8 - password.length());
    }
    
}
