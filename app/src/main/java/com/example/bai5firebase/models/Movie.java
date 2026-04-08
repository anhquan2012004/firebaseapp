package com.example.bai5firebase.models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String movieId;
    private String title;
    private String description;
    private String posterUrl;
    private String trailerUrl;
    private int duration;
    private String releaseDate;

    public Movie() {}

    public Movie(String movieId, String title, String description, String posterUrl, String trailerUrl, int duration, String releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}
