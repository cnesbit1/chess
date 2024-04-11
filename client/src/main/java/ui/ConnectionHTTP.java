package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHTTP {
    public String baseURL;
    public String authToken;
    public ConnectionHTTP(String authToken, String host, int port) {
        this.baseURL = String.format("http://%s:%s", host, port);
        this.authToken = authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String sendPostRequest(String endpoint, String requestData, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestData.getBytes());
        outputStream.flush();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(connection);
        } else {
            throw new Exception(String.format("Registration failed. Response code: %s", responseCode));
        }
    }
    public String sendGetRequest(String endpoint, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(connection);
        } else {
            throw new Exception(String.format("GET request failed. Response code: %s", responseCode));
        }
    }
    public String sendDeleteRequest(String endpoint, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("DELETE");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }
        connection.setDoOutput(false);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(connection);
        } else {
            throw new Exception(String.format("Logout failed. Response code: %s", responseCode));
        }
    }
    private String getString(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }
    public void sendPutRequest(String endpoint, String requestData, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        if (authToken != null && !authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", authToken);
        }

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestData.getBytes());
        outputStream.flush();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {}
        else { throw new Exception(String.format("PUT request failed. Response code: %s", responseCode)); }
    }
}
