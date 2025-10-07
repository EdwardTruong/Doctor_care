package com.example.doctorcare.application.dto;

import java.util.Map;

/*
 *  Fields :
 *  1. The 'from' field is the person who send email 
 *  2. The 'to' field mean will be sent to the address.
 *  3. The 'subject' field mean title of mail.
 *  4. The 'content' field clearly is content.
 *  5. The 'props' filed using Map<T,O> will carry some necessary content(username,password,jwt, tokens,..ect)
 *  
 */

public record DataMailDto (
        String from,
        String to,
        String subject,
        String content,
        Map<String, Object> props
) {


}
