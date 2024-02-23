package com.smartzie.payment.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookRequest {
    private String id;
    private String judul;
    private String penulis;
    private String penerbit;
    private Integer tahunTerbit;
    private BigDecimal harga;
}
