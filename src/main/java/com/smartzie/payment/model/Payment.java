package com.smartzie.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 50)
    @Column(name = "tipe_pembayaran", length = 50)
    private String tipePembayaran;

    @Column(name = "tanggal_pembayaran")
    private LocalDate tanggalPembayaran;

    @Column(name = "jumlah_bayar")
    private BigDecimal jumlahBayar;

    @Size(max = 100)
    @Column(name = "image", length = 100)
    private String image;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "siswa_id")
    private Long siswaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "virtual_account_id")
    private VirtualAccount virtualAccount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}