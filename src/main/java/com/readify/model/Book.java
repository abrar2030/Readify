package com.readify.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private Long id;
    @Column(name="name", nullable=false)
    @NotBlank(message="{book.name.notBlank}")
    private @NotBlank(message="{book.name.notBlank}") String name;
    @Column(name="price", nullable=false)
    @NotNull(message="{book.price.notBlank}")
    private @NotNull(message="{book.price.notBlank}") BigDecimal price;
    @Column(name="authors", nullable=false)
    @NotBlank(message="{book.authors.notBlank}")
    private @NotBlank(message="{book.authors.notBlank}") String authors;
    @Column(name="isbn", nullable=false)
    @NotBlank(message="{book.isbn.notBlank}")
    @Pattern(regexp="\\d{10}|\\d{13}", message="{book.isbn.size}")
    private @NotBlank(message="{book.isbn.notBlank}") @Pattern(regexp="\\d{10}|\\d{13}", message="{book.isbn.size}") String isbn;
    @Column(name="publisher", nullable=false)
    @NotBlank(message="{book.publisher.notBlank}")
    private @NotBlank(message="{book.publisher.notBlank}") String publisher;
    @Column(name="published_on", nullable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="{book.date.notNull}")
    private @NotNull(message="{book.date.notNull}") LocalDate publishedOn;

    public Book() {
    }

    public Book(Long id, String name, BigDecimal price, String authors, String isbn, String publisher, LocalDate publishedOn) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.authors = authors;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedOn = publishedOn;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAuthors() {
        return this.authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublishedOn() {
        return this.publishedOn;
    }

    public void setPublishedOn(LocalDate publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String toString() {
        return "Book [id=" + this.id + ", name=" + this.name + ", price=" + this.price + ", authors=" + this.authors + ", isbn=" + this.isbn + ", publisher=" + this.publisher + ", publishedOn=" + this.publishedOn + "]";
    }
}

