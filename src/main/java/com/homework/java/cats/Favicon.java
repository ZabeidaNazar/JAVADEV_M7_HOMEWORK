package com.homework.java.cats;

import com.homework.java.file.FileToBytes;
import com.homework.java.request.HttpRequest;
import com.homework.java.response.HttpResponse;

import java.io.IOException;

public class Favicon {
    private static byte[] favicon;
    public static void getFavicon(HttpRequest request, HttpResponse response) throws IOException {
        if (favicon == null) {
            response.setBody(favicon = FileToBytes.getFileInBytes("src/main/resources/favicon.ico"));
        } else {
            response.setBody(favicon);
        }

        response.addHeader("Content-Type", "image/x-icon");
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setStatusCode(200);

    }
}
