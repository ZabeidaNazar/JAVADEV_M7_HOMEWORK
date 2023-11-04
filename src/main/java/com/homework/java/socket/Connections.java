package com.homework.java.socket;

import com.homework.java.request.BadRequest;
import com.homework.java.request.HttpRequest;
import com.homework.java.response.HttpResponse;
import com.homework.java.web.Pages;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connections {

    public void createConnection(int port) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(200);

        ServerSocket socket = new ServerSocket(port);

        while(true) {
            System.out.println("Wait for connection...");
            Socket connection = socket.accept();
            System.out.println("Client with port - " + connection.getPort() + " successfully connected!");

            executor.submit(() -> connectionHandler(connection));
        }
    }

    private void connectionHandler(Socket connection) {
        try {
            Optional<HttpRequest> optionalHttpRequest = requestWorker(connection);
            if(optionalHttpRequest.isPresent()) {
                responseWorker(connection, optionalHttpRequest.get());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                System.out.println("Cannot close connection. Reason: " + e.getMessage());
            }
        }
    }

    private Optional<HttpRequest> requestWorker(Socket connection) throws Exception {
        String requestText = read(connection.getInputStream());
        if(!requestText.isEmpty()) {
            HttpRequest request = HttpRequest.mapTo(requestText);
            System.out.println(connection.getPort() + " - REQUEST => " + request);
            return Optional.of(request);
        } else {
            return Optional.empty();
        }
    }

    private void responseWorker(Socket connection, HttpRequest request) throws IOException {

        HttpResponse response = new HttpResponse();
        response.setProtocol(request.getProtocol());

        try {
            Pages.getService(request, response);

        } catch (BadRequest e) {
            Document document = Jsoup.parse(new File("web/notFound.html"));
            document.getElementsByClass("explain").first().appendText(e.getErrorMessage());

            response.setStatusCode(404);
            response.setStatusText("Not Found");
            response.setBody(document.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setStatusText("Server error");
            response.setBody(Pages.readPage("server-error.html"));
        }

        System.out.println(connection.getPort() + " - RESPONSE => " + response);
        connection.getOutputStream().write(response.makeResponse());
    }

    private String read(InputStream is) throws Exception {
        byte[] buffer = new byte[is.available()];
        int length = 0;
        while(is.available() > 0) {
            int read = is.read(buffer, length, is.available());
            length += read;
        }

        return new String(buffer, 0, length);
    }
}
