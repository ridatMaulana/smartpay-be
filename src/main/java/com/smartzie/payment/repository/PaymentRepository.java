package com.smartzie.payment.repository;

import com.smartzie.payment.model.Payment;
import com.smartzie.payment.model.VirtualAccount;
import com.smartzie.payment.request.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Boolean existsBySiswaIdAndTanggalPembayaranBetween(Long id, LocalDate start, LocalDate end);

    Boolean existsBySiswaIdAndTanggalPembayaranBetweenAndStatusIn(Long id, LocalDate start, LocalDate end, List<String> status);

    List<Payment> findAllByStatus(String status);

    List<Payment> findAllByStatusAndTanggalPembayaranBetween(String id, LocalDate start, LocalDate end);
    List<Payment> findAllBySiswaIdAndTanggalPembayaranBetween(Long id, LocalDate start, LocalDate end);

    Payment findByVirtualAccountAndStatus(VirtualAccount va, String status);
}