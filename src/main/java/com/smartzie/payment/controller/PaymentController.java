package com.smartzie.payment.controller;

import com.smartzie.payment.component.Role;
import com.smartzie.payment.request.PaymentRequest;
import com.smartzie.payment.request.TransferRequest;
import com.smartzie.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/payment")
//@PreAuthorize("hasRole('SISWA')")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

//    public DataTablesOutput<?> outputout(@RequestBody DataTablesInput input){
//
//    }

    @PostMapping("/viaTransfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request){
        return paymentService.viaTransfer(request);
    }

    @PostMapping("/createVA")
    public ResponseEntity<?> VAPayment(@RequestBody PaymentRequest request){
        return paymentService.createVA(request);
    }

    @PostMapping("/updateVA")
    public ResponseEntity<?> updateVA(@RequestBody PaymentRequest request){
        return paymentService.updateVA(request);
    }

    @PostMapping("/VA")
    public ResponseEntity<?> viaVA(@RequestBody PaymentRequest request){
        return paymentService.viaVa(request);
    }

}
