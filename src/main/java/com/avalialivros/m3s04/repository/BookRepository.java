package com.avalialivros.m3s04.repository;

import com.avalialivros.m3s04.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
