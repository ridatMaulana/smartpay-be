package com.smartzie.payment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String id;
    private String name;
    private String bank;
    private Long amount;
    private String status;
    private Long siswaId;
}
