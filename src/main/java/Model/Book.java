/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author franc
 */

public class Book {

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

    private int quantity;

    public Book(String title, String subtitle, Author author, int numPages, Category category, LocalDate publicationDate, AgeRange ageRange, Publisher publisher, String isbn, int quantity) {
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.numPages = numPages;
        this.category = category;
        this.publicationDate = publicationDate;
        this.ageRange = ageRange;
        this.publisher = publisher;
        this.isbn = isbn;
        this.quantity = quantity;
    }
    public Book(String title, String subtitle, int numPages, LocalDate publicationDate, String isbn, int quantity, Author author, Category category, AgeRange ageRange, Publisher publisher) {
        this.title = title;
        this.subtitle = subtitle;
        this.numPages = numPages;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.quantity = quantity;
        this.author = author;
        this.category = category;
        this.ageRange = ageRange;
        this.publisher = publisher;
    }

    public Book(int id, String title, String subtitle, Author author, int numPages, Category category, LocalDate publicationDate, AgeRange ageRange, Publisher publisher, String isbn, int quantity) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.numPages = numPages;
        this.category = category;
        this.publicationDate = publicationDate;
        this.ageRange = ageRange;
        this.publisher = publisher;
        this.isbn = isbn;
        this.quantity = quantity;
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


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n")
                .append("Título: ").append(title).append("\n")
                .append("Subtítulo: ").append(subtitle).append("\n")
                .append("Autor: ").append(author).append("\n")
                .append("Número de Páginas: ").append(numPages).append("\n")
                .append("Categoria: ").append(category.getCategoryName()).append("\n")
                .append("Data de Publicação: ").append(publicationDate).append("\n")
                .append("Faixa Etária: ").append(ageRange).append("\n")
                .append("Editora: ").append(publisher.getName()).append("\n")
                .append("Morada: ").append(publisher.getAddress()).append("\n")
                .append("ISBN: ").append(isbn).append("\n")
                .append("Quantidade: ").append(quantity).append("\n");
        return sb.toString();
    }



}
