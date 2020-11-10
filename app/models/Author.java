package models;

import java.beans.Transient;

/**
 * Created by indraneel on 07/11/20
 */
public class Author extends BaseIdEntity {

    public final static String JSON_ARRAY_NAME ="authors";
    private String firstName;
    private String lastName;
    private int posts;

    public Author() {
    }

    public Author(String firstName, String lastName, int posts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = posts;
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

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    @Override
    public String getJsonArrayName() {
        return JSON_ARRAY_NAME;
    }
}
