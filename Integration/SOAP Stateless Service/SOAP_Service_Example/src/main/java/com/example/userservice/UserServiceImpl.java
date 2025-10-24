package com.example.userservice;

import jakarta.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.userservice.model.ServiceResponse;
import com.example.userservice.model.User;

@WebService(
        serviceName = "UserService",
        portName = "UserServicePort",
        targetNamespace = "http://userservice.example.com/",
        endpointInterface = "com.example.userservice.UserService"
)
public class UserServiceImpl implements UserService {

    // In-memory storage for users (email -> User)
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public ServiceResponse registerUser(String email, String password, String nickname, String role) {
        System.out.println("Received: ");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Nickname: " + nickname);
        System.out.println("Role: " + role);

        try {
            if (users.containsKey(email)) {
                return new ServiceResponse(false, "User with email " + email + " already exists");
            }

            User newUser = new User(email, nickname, password, role);
            users.put(email, newUser);

            return new ServiceResponse(true, "User registered successfully", newUser);
        } catch (Exception e) {
            return new ServiceResponse(false, "Registration failed: " + e.getMessage());
        }
    }

    @Override
    public ServiceResponse loginUser(String email, String password) {
        try {
            User user = users.get(email);
            if (user == null) {
                return new ServiceResponse(false, "User not found");
            }

            if (!user.getPassword().equals(password)) {
                return new ServiceResponse(false, "Invalid password");
            }

            return new ServiceResponse(true, "Login successful", user);
        } catch (Exception e) {
            return new ServiceResponse(false, "Login failed: " + e.getMessage());
        }
    }

    @Override
    public ServiceResponse changeNickname(String email, String password, String newNickname) {
        try {
            User user = users.get(email);
            if (user == null) {
                return new ServiceResponse(false, "User not found");
            }

            if (!user.getPassword().equals(password)) {
                return new ServiceResponse(false, "Invalid password");
            }

            user.setNickname(newNickname);
            users.put(email, user);

            return new ServiceResponse(true, "Nickname changed successfully", user);
        } catch (Exception e) {
            return new ServiceResponse(false, "Nickname change failed: " + e.getMessage());
        }
    }

    @Override
    public ServiceResponse getUserRole(String email, String password) {
        try {
            User user = users.get(email);
            if (user == null) {
                return new ServiceResponse(false, "User not found");
            }

            if (!user.getPassword().equals(password)) {
                return new ServiceResponse(false, "Invalid password");
            }

            ServiceResponse response = new ServiceResponse(true, "Role retrieved successfully");
            User userWithoutPassword = new User(user.getEmail(), user.getNickname(), "", user.getRole());
            response.setUser(userWithoutPassword);

            return response;
        } catch (Exception e) {
            return new ServiceResponse(false, "Failed to get user role: " + e.getMessage());
        }
    }

    @Override
    public ServiceResponse removeUser(String email, String password) {
        try {
            User user = users.get(email);
            if (user == null) {
                return new ServiceResponse(false, "User not found");
            }

            if (!user.getPassword().equals(password)) {
                return new ServiceResponse(false, "Invalid password");
            }

            users.remove(email);
            return new ServiceResponse(true, "User removed successfully");
        } catch (Exception e) {
            return new ServiceResponse(false, "User removal failed: " + e.getMessage());
        }
    }
}