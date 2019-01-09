package com.blibli.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Document(collection = "member")
@Data
public class Member implements Serializable {

    @Id
    private String id;

    private String username;

    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;
}
