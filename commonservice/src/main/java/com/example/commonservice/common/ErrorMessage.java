package com.example.commonservice.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage{
    private String code;
    private String message;
    private HttpStatus status;
}
