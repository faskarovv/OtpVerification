package org.example.otpverification.service;

import org.example.otpverification.entity.Card;
import org.example.otpverification.entity.Status;
import org.example.otpverification.exceptionHandling.NoSuchUserExists;
import org.example.otpverification.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class OtpVerificationServiceTest {

    @Mock
    private FetchTokenService fetchTokenService;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private OtpVerificationService otpVerificationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void otpVerifiedSuccessfully(){
        String email = "email@gmail.com";
        String enteredOtp = "1234";
        String storedOtp = "hashed-1234";
        String enteredHashedOtp = "hashed-1234";


        Card exisitngCard = new Card();
        exisitngCard.setEmail(email);
        exisitngCard.setStatus(Status.INACTIVE.toString());

        when(fetchTokenService.getOtp(email)).thenReturn(storedOtp);
        when(fetchTokenService.hashOtp(enteredOtp)).thenReturn(enteredHashedOtp);
        when(fetchTokenService.removeOtp(email)).thenReturn(true);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(exisitngCard));


        boolean result = otpVerificationService.verifyOtp(email , enteredOtp);

        Card verifiedCard = new Card();
        verifiedCard.setEmail(email);
        verifiedCard.setStatus(Status.ACTIVE.toString());

        assertTrue(result);
        verify(fetchTokenService).getOtp(email);
        verify(fetchTokenService).hashOtp(enteredOtp);
        verify(fetchTokenService).removeOtp(email);
        verify(userRepo).findByEmail(email);
        verify(userRepo).save(argThat(card ->
                card.getEmail().equals(email) &&
                        card.getStatus().equals(Status.ACTIVE.toString())
        ));
    }
    @Test
    void cardDoesNotExist(){
        String email = "email@gmail.com";
        String enteredOtp = "1234";
        String storedOtp = "hashed-1234";
        String enteredHashedOtp = "hashed-1234";


        when(fetchTokenService.getOtp(email)).thenReturn(storedOtp);
        when(fetchTokenService.hashOtp(enteredOtp)).thenReturn(enteredHashedOtp);
        when(fetchTokenService.removeOtp(email)).thenReturn(true);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());


//        boolean result = otpVerificationService.verifyOtp(email , enteredOtp);

        NoSuchUserExists exception = assertThrows(
                NoSuchUserExists.class,
                () -> otpVerificationService.changeStatusOfUser(email)
        );
        assertEquals("no card exists with such email", exception.getMessage());
        verify(userRepo).findByEmail(email);
    }

    @Test
    void noOtpStored(){
        String email = "email@gmail.com";
        String enteredOtp = "1234";
        String storedOtp = " ";
        when(fetchTokenService.getOtp(email)).thenReturn(storedOtp);

        boolean result = otpVerificationService.verifyOtp(email , enteredOtp);

        assertFalse(result);
        verify(fetchTokenService).getOtp(email);
    }

    @Test
    void couldNotHashOtp(){
        String email = "email@gmail.com";
        String enteredOtp = "1234";
        String storedOtp = "hashed-1234";


        when(fetchTokenService.getOtp(email)).thenReturn(storedOtp);
        when(fetchTokenService.hashOtp(enteredOtp)).thenReturn(null);

        boolean result = otpVerificationService.verifyOtp(email , enteredOtp);
        assertFalse(result);
        verify(fetchTokenService).getOtp(email);
        verify(fetchTokenService).hashOtp(enteredOtp);
    }

    @Test
    void enteredAndStoredOtpNotEqual(){
        String email = "email@gmail.com";
        String enteredOtp = "1234";
        String storedOtp = "hashed-123";
        String enteredHashedOtp = "hashed-1234";

        Card exisitngCard = new Card();
        exisitngCard.setEmail(email);
        exisitngCard.setStatus(Status.INACTIVE.toString());

        when(fetchTokenService.getOtp(email)).thenReturn(storedOtp);
        when(fetchTokenService.hashOtp(enteredOtp)).thenReturn(enteredHashedOtp);
        when(fetchTokenService.removeOtp(email)).thenReturn(true);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(exisitngCard));

        boolean result = otpVerificationService.verifyOtp(email , enteredOtp);

        assertFalse(false);
        verify(fetchTokenService).getOtp(email);
        verify(fetchTokenService).hashOtp(enteredOtp);

    }

    @Test
    void failedToRemoveTheOtp(){
        String email = "email@gmail.com";
        String enteredOtp = "1234";
        String storedOtp = "hashed-1234";
        String enteredHashedOtp = "hashed-1234";

        when(fetchTokenService.getOtp(email)).thenReturn(storedOtp);
        when(fetchTokenService.hashOtp(enteredOtp)).thenReturn(enteredHashedOtp);
        when(fetchTokenService.removeOtp(email)).thenReturn(false);

        boolean result = otpVerificationService.verifyOtp(email , enteredOtp);


        assertFalse(false);
        verify(fetchTokenService).getOtp(email);
        verify(fetchTokenService).hashOtp(enteredOtp);
        verify(fetchTokenService).removeOtp(email);
    }

}