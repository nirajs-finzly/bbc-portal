package com.bbc.app.repository;

import com.bbc.app.model.NetBanking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NetBankingRepository extends JpaRepository<NetBanking, UUID> {
    boolean existsByAccountNumberAndIfscCode(String accountNumber, String ifscCode);

    NetBanking findTopByOrderByNetBankingIdDesc();

    NetBanking findByAccountNumberAndIfscCode(String accountNumber, String ifscCode);
}