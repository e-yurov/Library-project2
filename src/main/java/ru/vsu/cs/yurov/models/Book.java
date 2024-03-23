package ru.vsu.cs.yurov.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 1, max = 50, message = "Title size should be between 1 and 50")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author should not be empty")
    @Size(min = 5, max = 100, message = "Author name size should be between 5 and 100")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Author name should match pattern: Name Surname")
    @Column(name = "author")
    private String author;

    @Min(value = 0, message = "Year should be higher than 0")
    @Column(name = "year")
    private int year;

    @ManyToOne(targetEntity = Person.class)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person holder;

    @Column(name = "time_of_taking")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat
    private Date timeOfTaking;

    @Transient
    private boolean isOverdue;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book(){}

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getHolder() {
        return holder;
    }

    public void setHolder(Person holder) {
        this.holder = holder;
    }

    public Date getTimeOfTaking() {
        return timeOfTaking;
    }

    public void setTimeOfTaking(Date timeOfTaking) {
        this.timeOfTaking = timeOfTaking;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    @Override
    public String toString() {
        return title + ", " + author + ", " + year;
    }

    public boolean isAvailable() {
        return holder == null;
    }
}
