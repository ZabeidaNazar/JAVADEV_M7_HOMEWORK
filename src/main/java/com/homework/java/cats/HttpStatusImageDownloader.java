package com.homework.java.cats;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.homework.java.cats.Utils.*;

public class HttpStatusImageDownloader {
    public static String downloadStatusImage(int code) throws Exception {
        Files.createDirectories(Paths.get(DIRECTORY_FOR_SAVE));

        String path = DIRECTORY_FOR_SAVE + code + EXTENSION;

        File file = new File(path);
        if(file.exists()) {
            System.out.printf((FILE_ALREADY_EXIST_TEXT) + "%n", code);
            return path;
        }

        String url = HttpStatusChecker.getStatusImage(code);

        try(InputStream in = new URL(url).openStream()) {
            Files.copy(in, file.toPath());
            return path;
        }
    }
}
