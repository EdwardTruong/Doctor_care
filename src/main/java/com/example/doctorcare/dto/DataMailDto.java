package com.example.doctorcare.dto;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/*
 *  Fields :
 *  1. The 'from' field is the person who send email 
 *  2. The 'to' field mean will be sent to the address.
 *  3. The 'subject' field mean title of mail.
 *  4. The 'content' field clearly is content.
 *  5. The 'props' filed using Map<T,O> will carry some necessary content(username,password,jwt, tokens,..ect)
 *  
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataMailDto {

	String from;
	String to;
	String subject;
	String content;
	Map<String, Object> props;
}
