package com.avalialivros.m3s04.service;


import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import com.avalialivros.m3s04.model.Book;
import com.avalialivros.m3s04.model.Person;
import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.avalialivros.m3s04.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
        BDDMockito.given(this.personService.findByEmail(email))
                .willReturn(person);

        CreateBookDTO book = new CreateBookDTO("Clean Code", 2008);
        this.bookService.create(book, person);

        BDDMockito.then(this.bookRepository).should()
                .save(this.bookCaptor.capture());
        Book createdBook = this.bookCaptor.getValue();

        Assertions.assertEquals(book.title(), createdBook.getTitle());
        Assertions.assertEquals(book.yearOfPublication(), createdBook.getYearOfPublication());
        Assertions.assertEquals(email, createdBook.getCreatedBy().getEmail());
        Assertions.assertNotNull(createdBook.getGuid());
    }

}
