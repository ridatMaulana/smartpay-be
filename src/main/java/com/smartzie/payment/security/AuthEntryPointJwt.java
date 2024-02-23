package com.smartzie.payment.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartzie.payment.component.Response;
import com.smartzie.payment.component.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api")) {
            // Endpoint dalam ruang lingkup API
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Response<String> response2 = new Response<>();
            response2.setResponseCode(ResponseCode.JWTExp);


            ObjectMapper mapper = new ObjectMapper();
            OutputStream out = response.getOutputStream();
            mapper.writeValue(out, response2);
        } else {
            // Endpoint di luar ruang lingkup API
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            Response<String> response2 = new Response<>();
            response2.setResponseCode(ResponseCode.NO_END_POINT);


            ObjectMapper mapper = new ObjectMapper();
            OutputStream out = response.getOutputStream();
            mapper.writeValue(out, response2);
        }
    }

}
