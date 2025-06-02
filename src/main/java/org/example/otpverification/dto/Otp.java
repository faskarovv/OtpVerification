package org.example.otpverification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Otp {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String status;
        private String message;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "Required")
        @Email(message = "invalid format")
        private String email;
        @NotBlank(message = "Otp is required")
        private String otp;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenerationReq {
        @Email(message = "invalid email")
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResponse {
        @JsonProperty("jwtToken")
        private String otp;
    }
}
