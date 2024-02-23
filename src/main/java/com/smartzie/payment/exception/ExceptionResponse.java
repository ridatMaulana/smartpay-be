package com.smartzie.payment.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExceptionResponse {

    private String message;

    private String code;

}