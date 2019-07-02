package com.tubeproject.model.builder;

import com.tubeproject.controller.User;

import java.sql.Date;

public class UserBuilder {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String password;
    private int role;
    private String salt = "";

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRole(int role) {
        this.role = role;
        return this;
    }

    public UserBuilder setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public User createUser() {
        return new User(firstName, lastName, dateOfBirth, email, password, salt, role);
    }
}