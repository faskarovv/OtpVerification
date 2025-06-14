package org.example.otpverification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FetchTokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private FetchTokenService tokenService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void getOtp() {
        String email = "email@gmail.com";

        tokenService.getOtp(email);

        verify(valueOperations, times(1))
                .get(email);
    }

    @Test
    void getOtpReturnsRightToken(){
        String email = "email@gmai.com";
        String otp = "1234";

        when(valueOperations.get(email)).thenReturn(otp);

        String returnedOtp = tokenService.getOtp(email);

        assertEquals(otp , returnedOtp);
    }

    @Test
    void successfullyRemovedToken() {
        String email = "email@gmail";

        when(redisTemplate.delete(email)).thenReturn(true);

        tokenService.removeOtp(email);

        verify(redisTemplate, times(1))
                .delete(email);
    }

    @Test
    void failedToRemoveToken() {
        String email = "email@gmail";

        when(redisTemplate.delete(email)).thenReturn(false);

        tokenService.removeOtp(email);

        verify(redisTemplate, times(1))
                .delete(email);
    }

    @Test
    void failedToHashTheOtpWhenNull() {

        tokenService.hashOtp(null);

    }

    @Test
    void succeessfullyHashed(){
        String otp = "1234";
        String hashedOtp = tokenService.hashOtp(otp);

        assertNotNull(hashedOtp);
    }
}