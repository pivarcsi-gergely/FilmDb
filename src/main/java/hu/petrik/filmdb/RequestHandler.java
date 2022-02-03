package hu.petrik.filmdb;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    private RequestHandler() {
    }

    public static Response get(String url) throws IOException {
        HttpURLConnection httpConn = setupConnection(url);
        return getResponse(httpConn);
    }

    public static Response post(String url, String body) throws IOException {
        HttpURLConnection httpConn = setupConnection(url);
        httpConn.setRequestMethod("POST");
        addRequestBody(httpConn, body);
        return getResponse(httpConn);
    }

    public static Response put(String url, String body) throws IOException {
        HttpURLConnection httpConn = setupConnection(url);
        httpConn.setRequestMethod("PUT");
        addRequestBody(httpConn, body);
        return getResponse(httpConn);
    }

    public static Response delete(String url, String body) throws IOException {
        HttpURLConnection httpConn = setupConnection(url);
        httpConn.setRequestMethod("DELETE");
        return getResponse(httpConn);
    }


    private static HttpURLConnection setupConnection(String url) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) urlObject.openConnection();
        httpConn.setRequestProperty("Accept", "application/json");
        httpConn.setConnectTimeout(15000);
        httpConn.setReadTimeout(15000);
        return httpConn;
    }

    private static void addRequestBody(HttpURLConnection httpConn, String body) throws IOException {
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setDoOutput(true);
        OutputStream os = httpConn.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        bw.write(body);
        bw.flush();
        bw.close();
    }

    private static Response getResponse(HttpURLConnection httpConn) throws IOException {
        InputStream is;
        int responseCode = httpConn.getResponseCode();
        if (responseCode < 400) {
            is = httpConn.getInputStream();
        } else {
            is = httpConn.getErrorStream();
        }

        StringBuilder sBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String sor = br.readLine();
        while (sor != null) {
            sBuilder.append(sor);
            sor = br.readLine();
        }
        br.close();
        is.close();
        httpConn.disconnect();

        return new Response(responseCode, sBuilder.toString());
    }
}