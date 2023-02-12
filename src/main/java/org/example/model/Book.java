package org.example.model;

import lombok.*;

import java.util.List;

//POJO - plain old java object
@Getter
@Setter

public class Book {
    public Book(String title, Genre genre, double price, int amount) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.amount = amount;
    }

    public Book() {
    }

    private int id;
    private String title;
    private Author author;
    private Genre genre;
    private double price;
    private int amount;
}
