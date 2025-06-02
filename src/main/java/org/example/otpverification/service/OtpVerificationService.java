package org.example.otpverification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpVerificationService {


    private final FetchTokenService fetchTokenService;

    public boolean verifyOtp(String email, String enteredOtp) {
        String storedOtp = fetchTokenService.getOtp(email);

        if (storedOtp == null || storedOtp.isEmpty()) {
            log.warn("no otp stored ", email);
            return false;
        }

        String enteredHashOtp = fetchTokenService.hashOtp(enteredOtp);
        if (enteredHashOtp == null) {
            log.error("something is wrong with fetchtokenservice hashotp method");
        }

        boolean isValid = storedOtp.equals(enteredHashOtp);

        if (isValid) {
            boolean removedSucces = fetchTokenService.removeOtp(email);
            if (removedSucces) {
                log.info("validated and removed the otp for email", email);
                return true;
            } else {
                log.error("Otp is valid but could not remove", email);
                return false;
            }
        } else {
            log.error("could not validate ", email);
            return false;
        }
    }
}
