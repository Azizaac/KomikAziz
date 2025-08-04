package com.aziz.komikaziz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comic implements Serializable {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private String author;
    private String genre;
    private float rating;
    private int year;
    private int chapters;
    private String status;
    private String publisher;
    private boolean isFavorite;
    private String endpoint;

    private List<Chapter> chaptersList = new ArrayList<>();

    // Constructor
    public Comic() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getChapters() { return chapters; }
    public void setChapters(int chapters) { this.chapters = chapters; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public List<Chapter> getChaptersList() { return chaptersList; }
    public void setChaptersList(List<Chapter> chaptersList) { this.chaptersList = chaptersList; }

    public void setComicId(String comicId) {
        try {
            this.id = Integer.parseInt(comicId);
        } catch (NumberFormatException e) {
            this.id = -1; // default error value
        }
    }
}
