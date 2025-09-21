package com.example.doctorcare.core.domain;

public class BaseConstants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String ANONYMOUS = "anonymous";
    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE_CODE = "en";
    public static final String DEFAULT_COUNTRY_CODE = "EN";
    public static final String DEFAULT_TIMEZONE = "UTC";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm";

    public static final int PAGEABLE_DEFAULT_SIZE = 10;
    public static final String PAGEABLE_DEFAULT_SORT = "id";
}