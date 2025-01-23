package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.exceptions.NotValidArgumentException;
import org.springframework.stereotype.Component;

@Component
public class ControllerValidations {

    public void isValidId(Long id) throws NotValidArgumentException {
        if (id == null || id <= 0) {
            throw new NotValidArgumentException("Invalid ID");
        }
    }
}
