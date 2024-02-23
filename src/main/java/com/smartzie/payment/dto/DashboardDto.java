package com.smartzie.payment.dto;

import com.smartzie.payment.model.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardDto {
    private List<String> label;
    private List<Double> series;
}
