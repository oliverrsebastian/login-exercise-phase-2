package com.blibli.spring.controller;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private String status;
    private T value;
}
