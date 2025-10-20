package com.example.userservice.client;

import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;

import com.example.userservice.UserService;
import com.example.userservice.model.ServiceResponse;

public class UserServiceClient {
    private UserService userService;

    public UserServiceClient() throws Exception {
        URL wsdlURL = new URL("http://localhost:8080/userservice?wsdl");
        QName serviceName = new QName("http://userservice.example.com/", "UserService");
        Service service = Service.create(wsdlURL, serviceName);
        userService = service.getPort(UserService.class);
    }

    public void testAllOperations() {
        try {
            System.out.println("=== Testing UserService SOAP Client ===\n");

            // Test registerUser
            System.out.println("1. Testing registerUser:");
            ServiceResponse registerResponse = userService.registerUser(
                    "parahinkv@gmail.com",
                    "root",
                    "kirill",
                    "user");
            printResponse(registerResponse);

            // Test loginUser positive
            System.out.println("\n2. Testing loginUser (correct credentials):");
            ServiceResponse loginResponse1 = userService.loginUser(
                    "parahinkv@gmail.com",
                    "root");
            printResponse(loginResponse1);

            // Test loginUser negative
            System.out.println("\n2. Testing loginUser (invalid credentials):");
            ServiceResponse loginResponse2 = userService.loginUser(
                    "parahinkv@gmail.com",
                    "root1");
            printResponse(loginResponse2);

            // Test changeNickname
            System.out.println("\n4. Testing changeNickname:");
            ServiceResponse changeNickResponse = userService.changeNickname(
                    "parahinkv@gmail.com",
                    "root",
                    "kirill1");
            printResponse(changeNickResponse);

            // Test getUserRole
            System.out.println("\n5. Testing getUserRole:");
            ServiceResponse roleResponse = userService.getUserRole(
                    "parahinkv@gmail.com",
                    "root");
            printResponse(roleResponse);

            // Test register user that already exists
            System.out.println("\n6. Registering another user (should fail):");
            ServiceResponse registerResponse2 = userService.registerUser(
                    "alice@example.com",
                    "alicepass",
                    "alice",
                    "admin");
            printResponse(registerResponse2);

            // Test removeUser
            System.out.println("\n7. Testing removeUser:");
            ServiceResponse removeResponse = userService.removeUser(
                    "parahinkv@gmail.com",
                    "root");
            printResponse(removeResponse);

            // Verify removal
            System.out.println("\n8. Verifying removal by login removed user (should fail):");
            ServiceResponse verifyRemoval = userService.loginUser(
                    "parahinkv@gmail.com",
                    "root");
            printResponse(verifyRemoval);

        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printResponse(ServiceResponse response) {
        System.out.println("Success: " + response.isSuccess());
        System.out.println("Message: " + response.getMessage());
        if (response.getUser() != null) {
            System.out.println("User: " +
                    "email=" + response.getUser().getEmail() +
                    ", nickname=" + response.getUser().getNickname() +
                    ", role=" + response.getUser().getRole());
        }
    }

    public static void main(String[] args) {
        try {
            UserServiceClient client = new UserServiceClient();
            client.testAllOperations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}