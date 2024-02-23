package com.smartzie.payment.component;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartzie.payment.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.OutputStream;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionResponse> genericException(
            Exception ex) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(ex.getLocalizedMessage())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OutputStream out = response.getOutputStream();
        out.flush();
        Response<?> data = new Response<>();
        data.setResponseCode(ResponseCode.JWTExp);
        data.setMessage("UNAUTHORIZED");
        mapper.writeValue(out, data);
        }
}