/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author franc
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int counter = 0;
    private int id;
    private String title;
    private String subtitle;
    private Author author;
    private int numPages;
    private Category category;
    private LocalDate publicationDate;
    private AgeRange ageRange;
    private Publisher publisher;
    private String isbn;


    public Book(String title, String subtitle, Author author, int numPages, Category category, LocalDate publicationDate, AgeRange ageRange, Publisher publisher, String isbn) {
        this.id = counter++;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.numPages = numPages;
        this.category = category;
        this.publicationDate = publicationDate;
        this.ageRange = ageRange;
        this.publisher = publisher;
        this.isbn = isbn;
    }

    public Book() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n")
                .append("Title: ").append(title).append("\n")
                .append("Subtitle: ").append(subtitle).append("\n")
                .append("Author: ").append(author).append("\n")
                .append("Number of Pages: ").append(numPages).append("\n")
                .append("Category: ").append(category).append("\n")
                .append("Publication Date: ").append(publicationDate).append("\n")
                .append("Age Range: ").append(ageRange).append("\n")
                .append("Publisher: ").append(publisher).append("\n")
                .append("ISBN: ").append(isbn).append("\n");
        return sb.toString();
    }

}
