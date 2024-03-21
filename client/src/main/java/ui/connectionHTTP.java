package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class connectionHTTP {
    public String baseURL;

    public String authToken;

    public connectionHTTP(String authToken, String host, int port) {
        this.baseURL = "http://" + host + ":" + port;;
        this.authToken = authToken;
    }

    public String sendPostRequest(String endpoint, String requestData) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // Send request data
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestData.getBytes());
        outputStream.flush();

        // Read response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } else {
            throw new Exception("Registration failed. Response code: " + responseCode);
        }
    }
}
