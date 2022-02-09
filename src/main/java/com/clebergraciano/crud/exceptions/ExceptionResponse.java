package com.clebergraciano.crud.exceptions;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse implements Serializable {
    private static final long serialVersion = 1L;

    private Date timestamp;
    private String message;
    private String details;
}
