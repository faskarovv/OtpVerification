package org.example.otpverification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpverification.dto.Otp;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchTokenService {

    private final WebClient.Builder webclient;

    private String generateServiceUrl = "http://localhost:8081";

    public String getOtp(String email) {

        log.info("Fetching token from URL: ", generateServiceUrl, email);

        WebClient webClient = webclient.baseUrl(generateServiceUrl).build();


        try {
            Otp.TokenResponse response = webClient.get()
                    .uri("/internal/fetch-token/{email}", email)
                    .retrieve()
                    .bodyToMono(Otp.TokenResponse.class)
                    .block();

            if (response != null) {
                log.info("fetched token for email" + email);
                return response.getOtp();
            }
        } catch (Exception e) {
            log.error("could not fetch for email:" + email);
        }
        return null;
    }

    public boolean removeOtp(String email) {
        log.info("trying to remove token from this email:" + email);

        WebClient webClient = webclient.baseUrl(generateServiceUrl).build();

        try {
            Boolean remove = webClient.post()
                    .uri("/internal/remove-token/{email}", email)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(remove);
        } catch (Exception e) {
            log.error("could not remove the otp in fetchtokenservice", e.getMessage());
        }

        return Boolean.FALSE;
    }

    public String hashOtp(String enteredOtp) {
        if (enteredOtp == null || enteredOtp.trim().isEmpty()) {
            log.error("Entered otp is null or empty");
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = digest.digest(enteredOtp.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            log.error("no such alghortm exists in fetch token service");
        }
        return null;
    }
}
