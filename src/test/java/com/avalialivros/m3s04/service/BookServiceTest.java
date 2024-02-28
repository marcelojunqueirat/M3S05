package com.avalialivros.m3s04.service;


import com.avalialivros.m3s04.exceptions.BookNotFoundException;
import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import com.avalialivros.m3s04.model.Book;
import com.avalialivros.m3s04.model.Person;
import com.avalialivros.m3s04.model.transport.BookRatedDTO;
import com.avalialivros.m3s04.model.transport.BookRatedGuidDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.avalialivros.m3s04.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private PersonService personService;

    @Mock
    private BookRepository bookRepository;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Test
    void createBookReturnSuccess() throws PersonNotFoundException {
        String email = "teste01@example.com";
        Person person = new Person();
        person.setEmail(email);
        BDDMockito.given(this.personService.findByEmail(email)).willReturn(person);

        CreateBookDTO book = new CreateBookDTO("Clean Code", 2008);
        this.bookService.create(book, person);

        BDDMockito.then(this.bookRepository).should().save(this.bookCaptor.capture());
        Book createdBook = this.bookCaptor.getValue();

        Assertions.assertEquals(book.title(), createdBook.getTitle());
        Assertions.assertEquals(book.yearOfPublication(), createdBook.getYearOfPublication());
        Assertions.assertEquals(email, createdBook.getCreatedBy().getEmail());
        Assertions.assertNotNull(createdBook.getGuid());
    }

    @Test
    void listBooksReturnSuccess() {
        Person person = new Person();
        person.setEmail("teste01@example.com");
        CreateBookDTO bookOne = new CreateBookDTO("Clean Code", 2008);
        CreateBookDTO bookTwo = new CreateBookDTO("Programação Orientada a Objetos", 1999);

        List<Book> books = new ArrayList<>();
        books.add(new Book(bookOne, person));
        books.add(new Book(bookTwo, person));

        BDDMockito.given(bookRepository.findAll()).willReturn(books);

        List<BookRatedDTO> listReturned = bookService.list();

        Assertions.assertNotNull(listReturned);
        Assertions.assertEquals(books.size(), listReturned.size());
    }

    @Test
    void findBookByIdReturnSuccess() throws BookNotFoundException {
        Person person = new Person();
        person.setName("Teste 01");
        person.setEmail("teste01@example.com.br");

        String id = "9ef71c71-c76f-4a73-a7dc-11eff4717abe";
        Book bookMock = new Book();
        bookMock.setGuid(id);
        bookMock.setTitle("Clean Code");
        bookMock.setYearOfPublication(2008);
        bookMock.setCreatedBy(person);

        BDDMockito.given(this.bookRepository.findById(id)).willReturn(Optional.of(bookMock));
        BookRatedGuidDTO returnedBook = this.bookService.findByGuid(id);

        Assertions.assertEquals(bookMock.getTitle(), returnedBook.title());
        Assertions.assertEquals(bookMock.getYearOfPublication(), returnedBook.yearOfPublication());
        Assertions.assertNotNull(returnedBook);
    }
}
