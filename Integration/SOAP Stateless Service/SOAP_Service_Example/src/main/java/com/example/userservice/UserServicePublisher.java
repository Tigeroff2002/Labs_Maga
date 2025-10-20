package com.example.userservice;

import jakarta.xml.ws.Endpoint;

public class UserServicePublisher {
    public static void main(String[] args) {
        String address = "http://localhost:8080/userservice";
        Endpoint endpoint = Endpoint.publish(address, new UserServiceImpl());

        System.out.println("UserService is running at: " + address);
        System.out.println("WSDL available at: " + address + "?wsdl");

        // Keep the server running
        System.out.println("Press any key to stop the server...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        endpoint.stop();
        System.out.println("Server stopped");
    }
}