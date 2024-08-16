package com.deopraglabs.api_prysme.mapper.custom;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public Object convertXtoY(Object entity) {
        final Object obj = new Object();
        // Basicamente setar os campos do Model para o VO
        return obj;
    }

    public Object convertYtoX(Object entity) {
        final Object obj = new Object();
        // Basicamente setar os campos do VO para o Model
        return obj;
    }
}
