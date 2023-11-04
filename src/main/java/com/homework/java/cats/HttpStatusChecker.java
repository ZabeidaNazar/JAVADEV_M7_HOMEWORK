package com.homework.java.cats;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.homework.java.cats.Utils.*;

public class HttpStatusChecker {
    public static String getStatusImage(int code) throws Exception {
        String url = START_URL + code + EXTENSION;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        if(connection.getResponseCode() == 404) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_EXCEPTION_TEXT, code));
        }

        return url;
    }
}
