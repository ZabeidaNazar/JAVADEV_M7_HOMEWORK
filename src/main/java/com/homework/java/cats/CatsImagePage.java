package com.homework.java.cats;

import com.homework.java.file.FileToBase64;
import com.homework.java.request.BadRequest;
import com.homework.java.request.HttpRequest;
import com.homework.java.response.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.homework.java.cats.Utils.DIRECTORY_FOR_SAVE;

public class CatsImagePage {
    public static void genPageWithImage(HttpRequest request, HttpResponse response) throws Exception {
        if (request.getPath().split("/").length <= 2) {
            response.setStatusCode(303);
            response.addHeader("Location", "/");
            return;
        }

        int code;
        String catPath;
        String base64Cat;


        try {
            code = Integer.parseInt(request.getPath().split("/")[2]);
            if (code < 0 || code > 600) {
                throw new BadRequest("Статус код виходить за рамки можливого!");
            }
        } catch (NumberFormatException e) {
            throw new BadRequest("Ви ввели текст, а не цифри!");
        }

        try {
            catPath = HttpStatusImageDownloader.downloadStatusImage(code);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new BadRequest("Нажаль в художника закінчилася уява і він не придумав котика для цього статус коду!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        base64Cat = FileToBase64.encodeFile(catPath);

        Document document = Jsoup.parse(new File("web/http-code.html"));
        Element catImage = document.getElementsByClass("cat-image").first();
        catImage.attr("src", "data:image/png;base64," + base64Cat);

        response.setStatusCode(200);
        response.setStatusText("OK");
        response.setBody(document.toString());
    }

    public static void genPageWithAllImages(HttpRequest request, HttpResponse response) throws Exception {
        Files.createDirectories(Paths.get(DIRECTORY_FOR_SAVE));
        File[] files = new File(Utils.DIRECTORY_FOR_SAVE).listFiles();

        Document document;
        document = Jsoup.parse(new File("web/all-cats.html"));
        Element cats = document.getElementsByClass("cats").first();

        if (files.length == 0) {
            document.createElement("p").addClass("note").appendText("Ви не завантажили жодного котика!").appendTo(cats);
        } else {
            Element catsBlock;
            Element catsName;
            Element catImage;

            for (File file : files) {
                catsBlock = document.createElement("div").addClass("cat");

                catsName = document.createElement("p").appendText(file.getName());
                catImage = document.createElement("img")
                        .addClass("cat-image")
                        .attr("src", "data:image/png;base64," + FileToBase64.encodeFile(file.getAbsolutePath()));

                catsName.appendTo(catsBlock);
                catImage.appendTo(catsBlock);

                catsBlock.appendTo(cats);
            }
        }

        response.setStatusCode(200);
        response.setStatusText("OK");
        response.setBody(document.toString());
    }
}
