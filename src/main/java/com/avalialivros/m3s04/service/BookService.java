package com.avalialivros.m3s04.service;

import com.avalialivros.m3s04.exceptions.BookNotFoundException;
import com.avalialivros.m3s04.exceptions.BookRegisteredByThePersonException;
import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import com.avalialivros.m3s04.model.Book;
import com.avalialivros.m3s04.model.Person;
import com.avalialivros.m3s04.model.Rating;
import com.avalialivros.m3s04.model.transport.BookDTO;
import com.avalialivros.m3s04.model.transport.BookRatedDTO;
import com.avalialivros.m3s04.model.transport.BookRatedGuidDTO;
import com.avalialivros.m3s04.model.transport.RatingDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateRatingDTO;
import com.avalialivros.m3s04.repository.BookRepository;
import com.avalialivros.m3s04.repository.RatingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;
    private PersonService personService;
    private RatingRepository ratingRepository;

    public BookService(BookRepository bookRepository, PersonService personService, RatingRepository ratingRepository) {
        this.bookRepository = bookRepository;
        this.personService = personService;
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public BookDTO create(CreateBookDTO body, UserDetails userInSession) throws PersonNotFoundException {
        LOGGER.info("Iniciando a criação de um livro com o usuário de e-mail: {}", userInSession.getUsername());
        Person person = this.personService.findByEmail(userInSession.getUsername());
        Book book = this.bookRepository.save(new Book(body, person));
        LOGGER.info("Livro salvo, retornando-o...");
        return new BookDTO(book);
    }

    @Transactional
    public RatingDTO setRating(String guid, CreateRatingDTO body, UserDetails userInSession)
            throws BookNotFoundException, PersonNotFoundException, BookRegisteredByThePersonException {
        LOGGER.info("Iniciando a criação de uma avaliação com o usuário de e-mail: {}", userInSession.getUsername());
        Book book = this.bookRepository.findById(guid)
                .orElseThrow(() -> new BookNotFoundException("Livro não encontrado."));
        Person person = this.personService.findByEmail(userInSession.getUsername());

        if(book.getCreatedBy().getGuid() == person.getGuid()) {
            throw new BookRegisteredByThePersonException("Não é possível avaliar um livro cadastrado por você.");
        }

        Rating rating = this.ratingRepository.save(new Rating(body, person, book));
        LOGGER.info("Avaliação salva, retornando-a...");
        return new RatingDTO(rating);
    }

    public List<BookRatedDTO> list(){
        List<BookRatedDTO> listDTO = bookRepository.findAll().stream()
                .map(BookRatedDTO::new)
                .collect(Collectors.toList());
        return listDTO;
    }

    public BookRatedGuidDTO findByGuid(String guid) throws BookNotFoundException {
        return this.bookRepository.findById(guid).map(BookRatedGuidDTO::new)
                .orElseThrow(() -> new BookNotFoundException("Livro não encontrado."));
    }
}
