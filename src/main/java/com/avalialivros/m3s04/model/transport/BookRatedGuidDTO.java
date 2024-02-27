package com.avalialivros.m3s04.model.transport;

import com.avalialivros.m3s04.model.Book;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record BookRatedGuidDTO(String guid,
                               String title,
                               String createdByGuid,
                               Integer yearOfPublication,
                               Double averageGrade,
                               Map<Integer, Integer> countGrades,
                               Set<RatingDTO> rating) {

    public BookRatedGuidDTO(Book book) {
        this(book.getGuid(),
                book.getTitle(),
                book.getCreatedBy().getGuid(),
                book.getYearOfPublication(),
                book.getAverageGrades(),
                book.getCountGrades().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue()
                        )),
                book.getGrades().stream().map(RatingDTO::new).collect(Collectors.toSet())
        );
    }
}
