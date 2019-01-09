package com.blibli.spring.controller;

import com.blibli.spring.model.Book;
import com.blibli.spring.service.BookService;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/library")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/book/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(key = "#id", value = "book")
    public Book getBook(@PathVariable String id){
        return bookService.getBook(id);
    }

    @PostMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> saveBook(@RequestBody Book book){
        String success = bookService.saveBook(book);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue(success);
       return baseResponse;
    }

    @PutMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(key = "#book.id", value = "book")
    public BaseResponse<String> updateBook(@RequestBody    Book book){
        String success = bookService.saveBook(book);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue(success);
        return baseResponse;
    }

    @DeleteMapping(value = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(key = "#id", value = "book")
    public BaseResponse<String> deleteBook(@PathVariable String id){
        String success = bookService.deleteBook(id);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue(success);
        return baseResponse;
    }
}
