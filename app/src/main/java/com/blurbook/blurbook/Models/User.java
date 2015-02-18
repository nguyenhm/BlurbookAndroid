package com.blurbook.blurbook.Models;

/**
 * Created by Hoang on 2/17/2015.
 */
public class User
{
    String firstName,lastName,email,password;

    public User(String firstName, String lastName, String email,
                            String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {
        super();
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.password = null;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
