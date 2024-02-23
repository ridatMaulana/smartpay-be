package com.smartzie.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "virtual_account")
public class VirtualAccount {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "user_id")
    private Long userId;

    @Size(max = 36)
    @Column(name = "bank_code", length = 36)
    private String bankCode;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "expired_date")
    private LocalDate expiredDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}