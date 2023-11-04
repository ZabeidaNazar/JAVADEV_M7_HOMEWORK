package com.homework.java.cats;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusCheckerTest {

    @Test
    void test_shouldTrowException_whenCodeIsInvalid() {
        assertThrows(FileNotFoundException.class, () -> HttpStatusChecker.getStatusImage(8000));
    }

    @Test
    void test_shouldReturnUrl_whenCodeIsCorrect() throws Exception {
        assertEquals("https://http.cat/200.jpg", HttpStatusChecker.getStatusImage(200));
    }
}