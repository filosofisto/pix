package com.xti.bank.client.antifraud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Payee(
        @NotBlank(message = "Participant ISPB is required")
        @Pattern(regexp = "\\d{8}", message = "ISPB must be exactly 8 digits")
        String participantIspb,

        @NotBlank(message = "Key type is required")
        @Pattern(regexp = "CPF|CNPJ|EMAIL|PHONE|EVPIX",
                message = "Valid key types: CPF, CNPJ, EMAIL, PHONE, EVPIX")
        String keyType,

        @NotBlank(message = "Pix key value is required")
        @Size(max = 255, message = "Key value must not exceed 255 characters")
        String key,

        @NotBlank(message = "Name is required")
        @Size(max = 140, message = "Name must not exceed 140 characters")
        String name,

        @Pattern(regexp = "\\d{11}|\\d{14}", message = "CPF (11 digits) or CNPJ (14 digits)")
        String cpfCnpj,               // optional in some cases

        @Size(max = 60, message = "City must not exceed 60 characters")
        String city,

        @Size(max = 2, message = "State must be 2 characters")
        @Pattern(regexp = "[A-Z]{2}", message = "State must be uppercase 2-letter code")
        String state
) {}
