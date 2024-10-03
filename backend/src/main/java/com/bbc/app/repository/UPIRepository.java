package com.bbc.app.repository;

import com.bbc.app.model.UPI;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UPIRepository extends JpaRepository<UPI, UUID> {
    boolean existsByUpiIdValue(String upiId);

    UPI findTopByOrderByUpiIdDesc();
}
