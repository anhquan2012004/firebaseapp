package com.example.bai5firebase.models;

public class Seat {
    private String name;
    private boolean isBooked;
    private boolean isSelected;

    public Seat(String name, boolean isBooked) {
        this.name = name;
        this.isBooked = isBooked;
        this.isSelected = false;
    }

    public String getName() { return name; }
    public boolean isBooked() { return isBooked; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
