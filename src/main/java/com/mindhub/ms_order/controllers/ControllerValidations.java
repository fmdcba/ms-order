package com.mindhub.ms_order.controllers;

import com.mindhub.ms_order.exceptions.NotValidArgument;
import org.springframework.stereotype.Component;

@Component
public class ControllerValidations {

    public void isValidId(Long id) throws NotValidArgument {
        if (id == null || id <= 0) {
            throw new NotValidArgument("Invalid ID");
        }
    }
}
