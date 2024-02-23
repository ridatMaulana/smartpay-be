package com.smartzie.payment.controller;

import com.smartzie.payment.component.Response;
import com.smartzie.payment.component.ResponseCode;
import com.smartzie.payment.repository.PaymentRepository;
import com.smartzie.payment.request.BookRequest;
import com.smartzie.payment.request.CallbackRequest;
import com.smartzie.payment.request.LoginRequest;
import com.smartzie.payment.service.LoginService;
import com.smartzie.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> add(@RequestBody LoginRequest request){
        return loginService.login(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return loginService.loginSiswa(request.getUsername());
    }

    @PostMapping("/callback")
    public ResponseEntity<?> login(@RequestBody CallbackRequest request){
        return paymentService.paidCallback(request);
    }
}
