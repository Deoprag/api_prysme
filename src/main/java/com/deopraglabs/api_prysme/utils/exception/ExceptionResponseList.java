package com.deopraglabs.api_prysme.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Date timestamp;
    private List<String> message;
    private String details;
}
