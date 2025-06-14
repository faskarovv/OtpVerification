package org.example.otpverification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getOtp(String email) {

        return redisTemplate.opsForValue().get(email);
    }

    public boolean removeOtp(String email) {
        boolean removed = redisTemplate.delete(email);

        if (removed) {
            log.info("removed the token {} ", email);
            return true;
        } else {
            log.error("could not remove token");
            return false;
        }
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
