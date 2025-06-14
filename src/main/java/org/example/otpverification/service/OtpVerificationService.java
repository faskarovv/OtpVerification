package org.example.otpverification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpverification.entity.Card;
import org.example.otpverification.entity.Status;
import org.example.otpverification.exceptionHandling.NoSuchUserExists;
import org.example.otpverification.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class OtpVerificationService {


    private final FetchTokenService fetchTokenService;
    private final UserRepo userRepo;


    public boolean verifyOtp(String email, String enteredOtp) {
        String storedOtp = fetchTokenService.getOtp(email);

        if (!StringUtils.hasText(storedOtp)) {
            log.error("no otp stored {}", email);
            return false;
        }

        String enteredHashedOtp = fetchTokenService.hashOtp(enteredOtp);
        if (enteredHashedOtp == null) {
            log.error("something is wrong with fetchtokenservice hashotp method");
            return false;
        }


        if(validateAndRemoveOtp(email, enteredHashedOtp, storedOtp)){
            changeStatusOfUser(email);
            log.info("otp validated and status changed for emaik {}: " , email);
            return true;
        }else {
            log.error("could not validate the otp for email {}:" , email);
            return false;
        }
    }

    private boolean validateAndRemoveOtp(String email, String enteredHashedOtp, String storedOtp) {
        if (storedOtp.equals(enteredHashedOtp)) {
            boolean removed = fetchTokenService.removeOtp(email);
            if (removed) {
                log.info("otp validated and removed for {}", email);
                return true;
            } else {
                log.error("otp valid but failed to remove for {}", email);
                return false;
            }
        } else {
            log.warn("Invalid otp for {}", email);
            return false;
        }
    }

    public void changeStatusOfUser(String email) {
        Card verifiedUser = userRepo.findByEmail(email).orElseThrow(
                () -> new NoSuchUserExists("no card exists with such email")
        );

        verifiedUser.setStatus(Status.ACTIVE.toString());

        userRepo.save(verifiedUser);
    }

}
