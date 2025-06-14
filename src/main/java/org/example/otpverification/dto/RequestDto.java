package org.example.otpverification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    @NotBlank(message = "Required")
    @Email(message = "invalid format")
    private String email;
    @NotBlank(message = "Otp is required")
    private String otp;
}
