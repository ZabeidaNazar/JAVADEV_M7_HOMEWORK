package com.homework.java.web;

import com.homework.java.cats.CatsImagePage;
import com.homework.java.cats.Favicon;
import com.homework.java.file.FileToBytes;
import com.homework.java.request.HttpRequest;
import com.homework.java.response.HttpResponse;

import java.io.*;

public class Pages {
    public static void getService(HttpRequest request, HttpResponse response) throws Exception {
        String path = getPathItem(request.getPath(), 0);

        switch (path) {
            case "/favicon.ico" -> Favicon.getFavicon(request, response);
            case "/code" -> CatsImagePage.genPageWithImage(request, response);
            case "/cats" -> CatsImagePage.genPageWithAllImages(request, response);
            case "/" -> response.setBody(readPage("index.html"));
            default -> {
                try {
                    response.setBody(readPage(path.substring(1)));
                } catch (FileNotFoundException e) {
                    response.setBody(readPage("notFound.html"));
                }
            }
        }
    }

    public static String getPathItem(String path, int index) {
        String[] pathItems = path.substring(1).split("/", index + 2);

        if (pathItems.length < index)
            return "/";

        return "/" + pathItems[index];
    }

    public static byte[] readPage(String fileName) throws IOException {
        return FileToBytes.getFileInBytes("web/" + fileName);
    }
}
