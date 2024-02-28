package com.avalialivros.m3s04.service;


import com.avalialivros.m3s04.exceptions.BookNotFoundException;
import com.avalialivros.m3s04.exceptions.BookRegisteredByThePersonException;
import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import com.avalialivros.m3s04.model.Book;
import com.avalialivros.m3s04.model.Person;
import com.avalialivros.m3s04.model.Rating;
import com.avalialivros.m3s04.model.transport.BookRatedDTO;
import com.avalialivros.m3s04.model.transport.BookRatedGuidDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateRatingDTO;
import com.avalialivros.m3s04.repository.BookRepository;
import com.avalialivros.m3s04.repository.RatingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private PersonService personService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Captor
    private ArgumentCaptor<Rating> ratingCaptor;

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

    @Test
    void rateBookAndReturnSuccess() throws BookRegisteredByThePersonException, PersonNotFoundException, BookNotFoundException {
        String bookId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String userIdCreatedBook = UUID.randomUUID().toString();
        CreateRatingDTO rate = new CreateRatingDTO(5);

        Person personRating = new Person();
        personRating.setGuid(userId);
        personRating.setEmail("teste01@example.com");
        personRating.setName("Teste 01");

        Person personCreatedBook = new Person();
        personCreatedBook.setGuid(userIdCreatedBook);
        personCreatedBook.setEmail("teste02@example.com");
        personCreatedBook.setName("Teste 02");

        Book bookRated = new Book();
        bookRated.setGuid(bookId);
        bookRated.setTitle("Clean Code");
        bookRated.setYearOfPublication(2008);
        bookRated.setCreatedBy(personCreatedBook);

        BDDMockito.given(this.bookRepository.findById(bookId)).willReturn(Optional.of(bookRated));
        BDDMockito.given(this.personService.findByEmail(personRating.getEmail())).willReturn(personRating);

        this.bookService.setRating(bookId, rate, personRating);
        verify(this.ratingRepository).save(this.ratingCaptor.capture());
        Rating createdRated = this.ratingCaptor.getValue();

        Assertions.assertEquals(personRating.getEmail(), createdRated.getRatedBy().getEmail());
        Assertions.assertEquals(rate.grade(), createdRated.getGrade());
        Assertions.assertNotNull(createdRated.getGuid());
        Assertions.assertEquals(36, createdRated.getGuid().length());
    }
}
