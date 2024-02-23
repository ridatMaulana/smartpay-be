package com.smartzie.payment.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferRequest {
    private String id;
    private BigDecimal jumlahPembayaran;
    private String image;
    private Long siswaId;
    private String status;
}
