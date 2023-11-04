package com.homework.java.request;

public class BadRequest extends Exception {
    private String message = "Помилка невідома!";

    public BadRequest() {

    }

    public BadRequest(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }
}
