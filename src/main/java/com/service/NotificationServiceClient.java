package com.service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class NotificationServiceClient {
    private static final String BASE_URL = "https://microservice-notification-epdsg9cygpgzb4hr.southeastasia-01.azurewebsites.net/api/notification";

    public String getRestockNotifications() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL);
        
        Response response = target.request().get();
        String result = response.readEntity(String.class);
        
        response.close();
        return result;
    }
}