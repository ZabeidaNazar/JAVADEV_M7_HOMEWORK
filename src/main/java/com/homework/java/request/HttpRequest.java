package com.homework.java.request;

import lombok.*;
import com.homework.java.web.Method;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest {
    private Method method;
    private String path;
    private String protocol;
    private Map<String, String> headers = new LinkedHashMap<>();
    private String body;

    public static HttpRequest mapTo(String text) {
        HttpRequest request = new HttpRequest();
        String[] lines = text.replace("\r", "").split("\n");

        String[] firstLineParts = lines[0].split(" ");
        request.setMethod(Method.valueOf(firstLineParts[0]));
        request.setPath(firstLineParts[1]);
        request.setProtocol(firstLineParts[2]);

        int bodyStartLineNumber = -1;
        for(int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if(line.equals("")) {
                bodyStartLineNumber = i + 1;
                break;
            } else {
                String[] header = line.split(": ");
                request.getHeaders().put(header[0], header[1]);
            }
        }

        if (bodyStartLineNumber > 0) {
            StringBuilder sb = new StringBuilder();
            for(int i = bodyStartLineNumber; i < lines.length; i++) {
                sb.append(lines[i]).append("\n");
            }
            request.setBody(sb.toString());
        }

        return request;
    }
}
