package com.antifraud.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Payer(
        @NotBlank(message = "Participant ISPB is required")
        @Pattern(regexp = "\\d{8}", message = "ISPB must be exactly 8 digits")
        String participantIspb,

        @NotBlank(message = "Account type is required")
        @Size(max = 4, message = "Account type must not exceed 4 characters")
        String accountType,           // e.g., CACC, SVGS

        @NotBlank(message = "Branch code is required")
        @Size(max = 10, message = "Branch code must not exceed 10 characters")
        String branch,

        @NotBlank(message = "Account number is required")
        @Size(max = 20, message = "Account number must not exceed 20 characters")
        String accountNumber,

        @NotBlank(message = "CPF/CNPJ is required")
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "CPF (11 digits) or CNPJ (14 digits)")
        String cpfCnpj,

        @NotBlank(message = "Name is required")
        @Size(max = 140, message = "Name must not exceed 140 characters")
        String name,

        @Size(max = 60, message = "City must not exceed 60 characters")
        String city,

        @Size(max = 2, message = "State must be 2 characters")
        @Pattern(regexp = "[A-Z]{2}", message = "State must be uppercase 2-letter code")
        String state
) {}
