package com.librarysystem.librarysystem;

public class Book {
    public int id;
    public String author;
    public String name;
    public String genre;
    public int number;

    public Book(String author, String name, String genre, int number){
        this.author = author;
        this.name = name;
        this.genre = genre;
        this.number = number;
    }

    public Book(int id, String author, String name, String genre, int number){
        this.id = id;
        this.author = author;
        this.name = name;
        this.genre = genre;
        this.number = number;
    }

    @Override
    public String toString() {
        return id + " - " + author + " - " + name + " - " + genre + " - " + number;
    }
}
