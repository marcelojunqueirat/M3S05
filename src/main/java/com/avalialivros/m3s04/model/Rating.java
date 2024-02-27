package com.avalialivros.m3s04.model;

import com.avalialivros.m3s04.model.transport.operations.CreateRatingDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Rating {
    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String guid;

    @Column(nullable = false)
    private Integer grade;

    @ManyToOne
    @JoinColumn(name = "rated_by", referencedColumnName = "guid", nullable = false)
    private Person ratedBy;

    @ManyToOne
    @JoinColumn(name = "rated_book", referencedColumnName = "guid", nullable = false)
    private Book ratedBook;

    public Rating() {
    }

    public Rating(CreateRatingDTO body, Person person, Book book) {
        this.guid = UUID.randomUUID().toString();
        this.grade = body.grade();
        this.ratedBy = person;
        this.ratedBook = book;
    }

    public String getGuid() {
        return guid;
    }

    public Integer getGrade() {
        return grade;
    }

    public Person getRatedBy() {
        return ratedBy;
    }

    public Book getRatedBook() {
        return ratedBook;
    }
}
