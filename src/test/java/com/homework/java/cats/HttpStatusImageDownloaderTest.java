package com.homework.java.cats;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusImageDownloaderTest {

    @Test
    void test_trowException_whenCodeIsInvalid() {
        assertThrows(FileNotFoundException.class, () -> HttpStatusImageDownloader.downloadStatusImage(1000));
    }

    @Test
    void test_downloadImage_whenCodeIsCorrect() throws Exception {
        HttpStatusImageDownloader.downloadStatusImage(200);
        assertTrue(new File("cats/200.jpg").exists());
    }

    @Test
    void test_mustReturnPathToImage_whenCodeIsCorrect() throws Exception {
        assertEquals("cats/200.jpg", HttpStatusImageDownloader.downloadStatusImage(200));
    }
}