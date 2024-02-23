package com.smartzie.payment.controller;

import com.smartzie.payment.component.Response;
import com.smartzie.payment.component.ResponseCode;
import com.smartzie.payment.dto.DashboardDto;
import com.smartzie.payment.request.LoginRequest;
import com.smartzie.payment.request.PaymentRequest;
import com.smartzie.payment.request.ReportRequest;
import com.smartzie.payment.request.TransferRequest;
import com.smartzie.payment.service.AdminService;
import com.smartzie.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    AdminService adminService;

    @PostMapping("/process")
    public ResponseEntity<?> get(){
        return paymentService.process();
    }

    @PostMapping("/valid")
    public ResponseEntity<?> valid(@RequestBody ReportRequest request){
        return paymentService.valid(request);
    }

    @PostMapping("/validasi")
    public ResponseEntity<?> validasi(@RequestBody TransferRequest request){
        if (request.getId().isEmpty()){
            Response<?> response = new Response<>();
            response.setResponseCode(ResponseCode.BAD_REQUEST);
            return ResponseEntity.badRequest().body(response);
        }
        return paymentService.validasi(request);
    }

    @PostMapping("/dashboard/jumlahTransaksi")
    public ResponseEntity<?> dashboard(){
        Response<DashboardDto> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        response.setData(adminService.dashboardJumlahTransaksi());
        return ResponseEntity.ok(response);
    }

}
