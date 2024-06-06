package com.example.myapplication;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private String publishedDate;
    private int quantity;

    public Book() {
        // Default constructor required for Firestore deserialization
    }

    public Book(String title, String author, String isbn, String publishedDate, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public int getQuantity() {
        return quantity;
    }
}
