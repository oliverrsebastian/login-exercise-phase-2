package com.blibli.spring.service;

import com.blibli.spring.model.Book;

import java.util.List;

public interface BookService {
    Book getBook(String id);

    List<Book> getAllBooks();

    String saveBook(Book book);

    String deleteBook(String id);
}
