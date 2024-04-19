package org.example.miniprojectspring.service;

import lombok.AllArgsConstructor;
import org.example.miniprojectspring.model.dto.OptsDTO;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
public class OptGenerator {


    public static OptsDTO generateOTP(Integer length, UUID userId) {
        final long expiryInterval = 5L * 60 * 1000;
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return new OptsDTO(otp.toString(), new Date(), new Date(System.currentTimeMillis() + expiryInterval),false,userId);
    }

}
