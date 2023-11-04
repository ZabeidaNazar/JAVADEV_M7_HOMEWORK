package com.homework.java;

import com.homework.java.socket.Connections;

public class Main {
    public static void main (String[] args) throws Exception {
        new Connections().createConnection(9999);
    }
}