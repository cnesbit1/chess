package ui;

import java.io.BufferedReader;
import java.io.IOException;
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

    public String sendRegisterRequest(String username, String password, String email) throws IOException {
        String endpoint = "/register";
        String requestBody = "username=" + username + "&password=" + password + "&email=" + email;

        HttpURLConnection connection = createConnection(endpoint, "POST", requestBody);
        return handleResponse(connection);
    }

    private HttpURLConnection createConnection(String endpoint, String method, String requestBody) throws IOException {
        URL url = new URL(baseURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        if (requestBody != null) {
            sendRequestBody(connection, requestBody);
        }

        return connection;
    }

    private void sendRequestBody(HttpURLConnection connection, String requestBody) throws IOException {
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String handleResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readResponse(connection);
        } else {
            throw new IOException("Registration failed with status code: " + responseCode);
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
