package com.smartzie.payment.service;

import com.smartzie.payment.dto.DashboardDto;
import com.smartzie.payment.model.Payment;
import com.smartzie.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final PaymentRepository paymentRepository;

    public AdminService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public DashboardDto dashboardJumlahTransaksi(){
        List<Payment> data = paymentRepository.findAll();
        DashboardDto dto = new DashboardDto();
        List<String> label = new ArrayList<>();
        List<Double> series = new ArrayList<>();
        Double total = (double) data.stream().filter(payment -> !payment.getStatus().equals("Invalid")).toList().size();
        Double progress = 0D, process = 0D, valid = 0D;
        label.add(0, "Process");
        label.add(1, "Progress");
        label.add(2, "Valid");
        for (Payment pay : data){
            if (pay.getStatus().equals("Process")){
                process = process + 1D;
            }else if(pay.getStatus().equals("Progress")){
                progress = progress + 1D;
            }else if(pay.getStatus().equals("Valid")){
                valid = valid + 1D;
            }
        }
        series.add(0, (process/total)*100);
        series.add(1, (progress/total)*100);
        series.add(2, (valid/total)*100);
        dto.setLabel(label);
        dto.setSeries(series);
        return dto;
    }

    public DashboardDto dashboardJumlahPemasukan(){
        List<Payment> data = paymentRepository.findAll();
        DashboardDto dto = new DashboardDto();
        List<String> label = new ArrayList<>();
        List<Double> series = new ArrayList<>();
        for (Payment pay : data){
            if (pay.getStatus().equals("Process")){

            }else if(pay.getStatus().equals("Progress")){

            }else if(pay.getStatus().equals("Valid")){

            }
        }
        return dto;
    }
}
