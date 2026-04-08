package com.example.bai5firebase.models;

public class Showtime {
    private String showtimeId;
    private String movieId;
    private String theaterId;
    private String startTime;
    private String endTime;
    private double price;

    public Showtime() {}

    public Showtime(String showtimeId, String movieId, String theaterId, String startTime, String endTime, double price) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
