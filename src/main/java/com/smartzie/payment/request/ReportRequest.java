package com.smartzie.payment.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ReportRequest {
    private String startDate;
    private String endDate;
}
