package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by indraneel on 07/11/20
 */
public class Post extends BaseIdEntity {
    public final static String JSON_ARRAY_NAME ="posts";

    private String title;
    private String author;
    private int views;
    private int reviews;

    public Post() {
    }

    public Post(String title, String author, int views, int reviews) {
        this.title = title;
        this.author = author;
        this.views = views;
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    @Override
    public String getJsonArrayName() {
        return JSON_ARRAY_NAME;
    }

}
