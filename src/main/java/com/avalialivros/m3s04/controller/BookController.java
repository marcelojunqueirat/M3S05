package com.avalialivros.m3s04.controller;

import com.avalialivros.m3s04.exceptions.BookNotFoundException;
import com.avalialivros.m3s04.exceptions.BookRegisteredByThePersonException;
import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import com.avalialivros.m3s04.model.transport.BookDTO;
import com.avalialivros.m3s04.model.transport.BookRatedDTO;
import com.avalialivros.m3s04.model.transport.BookRatedGuidDTO;
import com.avalialivros.m3s04.model.transport.RatingDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateRatingDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.avalialivros.m3s04.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDTO> create(@AuthenticationPrincipal UserDetails userInSession,
                                          @Valid @RequestBody CreateBookDTO body,
                                          UriComponentsBuilder uriComponentsBuilder)
            throws PersonNotFoundException {
        BookDTO response = this.bookService.create(body, userInSession);
        return ResponseEntity.created(uriComponentsBuilder.path("/book/{id}").buildAndExpand(response.guid()).toUri()).body(response);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<RatingDTO> setRating(@PathVariable("id") String guid,
                                               @Valid @RequestBody CreateRatingDTO body,
                                               @AuthenticationPrincipal UserDetails userInSession)
            throws BookRegisteredByThePersonException, PersonNotFoundException, BookNotFoundException {
        RatingDTO response = this.bookService.setRating(guid, body, userInSession);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookRatedDTO>> findAll(){
        List<BookRatedDTO> response = this.bookService.list();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookRatedGuidDTO> findByGuid(@PathVariable("id") String guid) throws BookNotFoundException {
        BookRatedGuidDTO response = this.bookService.findByGuid(guid);
        return ResponseEntity.ok(response);
    }
}
