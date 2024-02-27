package com.avalialivros.m3s04.model;

import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Book {
    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String guid;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "guid", nullable = false)
    private Person createdBy;

    @Column(nullable = false)
    private Integer yearOfPublication;

    @OneToMany(mappedBy = "ratedBook")
    private Set<Rating> grades = new HashSet<>();

    @Transient
    private Map<Integer, Integer> countGrades = new HashMap<>();

    public Book() {
    }

    public Book(CreateBookDTO book, Person person) {
        this.guid = UUID.randomUUID().toString();
        this.title = book.title();
        this.createdBy = person;
        this.yearOfPublication = book.yearOfPublication();
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public Set<Rating> getGrades() {
        return grades;
    }

    public Double getAverageGrades() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = grades.stream()
                .mapToDouble(rating -> rating.getGrade())
                .sum();

        return sum / grades.size();
    }

    public Map<Integer, Integer> getCountGrades(){
        for (Rating grade : grades) {
            countGrades.put(grade.getGrade(),
                    countGrades.getOrDefault(grade.getGrade(), 0)
                            + 1);
        }

        return countGrades;
    }
}
