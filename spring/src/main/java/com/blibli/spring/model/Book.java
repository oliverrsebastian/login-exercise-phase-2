package com.blibli.spring.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "book")
public class Book implements Serializable {

    private String id;

    private String name;

    private String type;
}
