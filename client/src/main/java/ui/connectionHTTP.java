package ui;

import model.AuthData;

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

    public String sendGetRequest(String endpoint, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

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
            throw new Exception("GET request failed. Response code: " + responseCode);
        }
    }

    public String sendDeleteRequest(String endpoint, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("DELETE");
        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken); // Include authentication token in the header
        }
        connection.setDoOutput(false); // No request body for DELETE requests

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
            throw new Exception("Logout failed. Response code: " + responseCode);
        }
    }

    public void sendPutRequest(String endpoint, String requestData, String authToken) throws Exception {
        String url = this.baseURL + endpoint;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // Set authentication token in the request header if provided
        if (authToken != null && !authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", authToken);
        }

        // Send request data
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestData.getBytes());
        outputStream.flush();

        // Read response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Process successful response if needed
        } else {
            // Handle error response
            throw new Exception("PUT request failed. Response code: " + responseCode);
        }
    }
}
