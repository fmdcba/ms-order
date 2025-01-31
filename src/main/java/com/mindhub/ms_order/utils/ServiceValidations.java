package com.mindhub.ms_order.utils;

import com.mindhub.ms_order.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceValidations {

    @Autowired
    JwtUtils jwtUtils;

    public Long getUserId() {
        String token = jwtUtils.getJwtToken();
        return jwtUtils.extractId(token);
    }

    public boolean isAdmin() {
        String token = jwtUtils.getJwtToken();
        String userRole = jwtUtils.extractRole(token);

        return userRole.equals("ADMIN");
    }
}
