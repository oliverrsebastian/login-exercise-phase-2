package com.blibli.spring.service;

import com.blibli.spring.model.Book;
import com.blibli.spring.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book getBook(String id){
        return bookRepository.findOne(id);
    }

    @Override
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public String saveBook(Book book){
        bookRepository.save(book);
        return "Save Succeed!";
    }

    @Override
    @Transactional
    public String deleteBook(String id){
        bookRepository.delete(id);
        return "Delete Success!";
    }
}
