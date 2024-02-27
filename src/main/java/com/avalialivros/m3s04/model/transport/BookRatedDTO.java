package com.avalialivros.m3s04.model.transport;

import com.avalialivros.m3s04.model.Book;

public record BookRatedDTO(String guid,
                           String title,
                           String createdByGuid,
                           Integer yearOfPublication,
                           Double averageGrade) {
    public BookRatedDTO(Book book) {
        this(book.getGuid(),
                book.getTitle(),
                book.getCreatedBy().getGuid(),
                book.getYearOfPublication(),
                book.getAverageGrades()
        );
    }
}
