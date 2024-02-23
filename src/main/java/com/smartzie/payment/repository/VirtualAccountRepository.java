package com.smartzie.payment.repository;

import com.smartzie.payment.model.VirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, String> {
    Boolean existsByBankCodeAndUserId(String bank, Long user);

    VirtualAccount findByUserIdAndBankCode(Long user, String bank);
}
