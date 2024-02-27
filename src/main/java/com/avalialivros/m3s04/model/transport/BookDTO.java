package com.avalialivros.m3s04.model.transport;


import com.avalialivros.m3s04.model.Book;

public record BookDTO(String guid,
                      String title,
                      PersonDTO createdBy,
                      Integer yearOfPublication) {
    public BookDTO(Book book){
        this(book.getGuid(),
                book.getTitle(),
                new PersonDTO(book.getCreatedBy()),
                book.getYearOfPublication());
    }
}
