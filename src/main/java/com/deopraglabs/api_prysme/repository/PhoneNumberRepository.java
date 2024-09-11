package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    PhoneNumber findByNumber(String number);
}
