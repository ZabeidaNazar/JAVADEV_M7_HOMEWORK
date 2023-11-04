package com.homework.java.file;

import java.io.FileInputStream;
import java.io.IOException;

public class FileToBytes {
    public static byte[] getFileInBytes(String filename) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filename);

        byte[] buffer = new byte[fileInputStream.available()];
        int length = 0;
        while(fileInputStream.available() > 0) {
            int read = fileInputStream.read(buffer, length, fileInputStream.available());
            length += read;
        }

        fileInputStream.close();

        return buffer;
    }
}
