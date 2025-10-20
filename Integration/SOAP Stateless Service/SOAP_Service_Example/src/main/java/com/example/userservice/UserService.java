package com.example.userservice;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;

import com.example.userservice.model.ServiceResponse;
import com.example.userservice.model.User;

@WebService(name = "UserService", targetNamespace = "http://userservice.example.com/")
public interface UserService {

    @WebMethod
    @WebResult(name = "response")
    ServiceResponse registerUser(
            @WebParam(name = "email") @XmlElement(required = true) String email,
            @WebParam(name = "password") @XmlElement(required = true) String password,
            @WebParam(name = "nickname") @XmlElement(required = true) String nickname,
            @WebParam(name = "role") @XmlElement(required = true) String role
    );

    @WebMethod
    @WebResult(name = "response")
    ServiceResponse loginUser(
            @WebParam(name = "email") @XmlElement(required = true) String email,
            @WebParam(name = "password") @XmlElement(required = true) String password
    );

    @WebMethod
    @WebResult(name = "response")
    ServiceResponse changeNickname(
            @WebParam(name = "email") @XmlElement(required = true) String email,
            @WebParam(name = "password") @XmlElement(required = true) String password,
            @WebParam(name = "newNickname") @XmlElement(required = true) String newNickname
    );

    @WebMethod
    @WebResult(name = "response")
    ServiceResponse getUserRole(
            @WebParam(name = "email") @XmlElement(required = true) String email,
            @WebParam(name = "password") @XmlElement(required = true) String password
    );

    @WebMethod
    @WebResult(name = "response")
    ServiceResponse removeUser(
            @WebParam(name = "email") @XmlElement(required = true) String email,
            @WebParam(name = "password") @XmlElement(required = true) String password
    );
}