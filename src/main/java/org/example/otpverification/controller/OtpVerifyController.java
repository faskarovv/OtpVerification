package org.example.otpverification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpverification.service.FetchTokenService;
import org.example.otpverification.service.OtpVerificationService;
import org.example.otpverification.dto.Otp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpVerifyController {

    private final OtpVerificationService otpVerificationService;
    private final FetchTokenService fetchTokenService;
    private final WebClient.Builder webClient;

    @PostMapping("/verify")
    public ResponseEntity<Otp.Response> verifyOtp(@Valid @RequestBody Otp.Request request) {

        boolean isValid = otpVerificationService.verifyOtp(request.getEmail(), request.getOtp());

        if (isValid) {
            return ResponseEntity.ok(new Otp.Response("accepted", "otp deleted from database"));
        } else return ResponseEntity.badRequest().body(new Otp.Response("denied", "invalid otp"));
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestBody Otp.GenerationReq generationReq) {

        try {
            String url = "http://localhost:8081/otp/generate";


            WebClient webclient = webClient.baseUrl(url).build();
            webclient.post()
                    .uri(url)
                    .bodyValue(generationReq)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();


        } catch (Exception e) {
            log.error("otp could not be sent to email:" + generationReq.getEmail());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("otp sent");
    }
}
