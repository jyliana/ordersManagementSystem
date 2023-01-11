package com.example.system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppError {
    private int statusCode;
    private String message;

}
