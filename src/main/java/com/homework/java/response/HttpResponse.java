package com.homework.java.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {

    private static final String SPACE = " ";
    private static final String HEADER_SPLITTER = ": ";
    private static final String NEW_LINE = "\n";

    private String protocol;
    private int statusCode;
    private String statusText;
    private byte[] body;
    private Map<String, String> headers = new LinkedHashMap<>();

    public void setBody(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public byte[] makeResponse() {
        StringBuffer result = new StringBuffer();
        // start line
        result.append(this.protocol).append(SPACE).append(this.statusCode).append(SPACE)
                .append(statusText).append(NEW_LINE);

        // headers
        this.headers.forEach((key, value) -> {
            result.append(key).append(HEADER_SPLITTER).append(value).append(NEW_LINE);
        });
        // add content length header
        result.append("Content-Length: ").append(body == null ? 0 : body.length).append(NEW_LINE);
        // add content type header
        if (headers.get("Content-Type") == null)
            result.append("Content-Type: text/html; charset=utf-8").append(NEW_LINE);

        // empty line between headers and body
        result.append(NEW_LINE);

        //body
        //result.append(body);

        byte[] withoutBody = result.toString().getBytes(StandardCharsets.UTF_8);

        if (body != null) {
            byte[] res = new byte[withoutBody.length + body.length];
            System.arraycopy(withoutBody, 0, res, 0, withoutBody.length);
            System.arraycopy(body, 0, res, withoutBody.length, body.length);
            return res;
        } else {
            return withoutBody;
        }
    }

    @Override
    public String toString() {
        return "HttpResponse(" +
                "protocol='" + protocol + '\'' +
                ", statusCode=" + statusCode +
                ", statusText='" + statusText + '\'' +
                ", body='" + (body != null && body.length > 0) + '\'' +
                ", headers=" + headers +
                ')';
    }
}
