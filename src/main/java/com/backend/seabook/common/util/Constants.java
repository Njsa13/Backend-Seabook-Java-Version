package com.backend.seabook.common.util;

public class Constants {
    public static final String API_VERSION = "/v1";
    public static final String BASE_URL = "/api" + API_VERSION;

    public static final class AuthPats {
        public static final String AUTH_PATS = BASE_URL + "/auth";
    }

    public static final class ControllerMessage {
        public static final String BOOK_NOT_FOUND = "Book Not Found";
        public static final String USER_NOT_FOUND = "User Not Found";
        public static final String USER_ALREADY_VERIFIED = "User Already Verified";
        public static final String USER_NOT_VERIFIED = "User Is Not Verified";
        public static final String TOKEN_EXPIRED = "Token Expired";
        public static final String TOKEN_NOT_FOUND = "Token Not Found";
        public static final String CATEGORY_NOT_FOUND = "Category Not Found";
    }
}
