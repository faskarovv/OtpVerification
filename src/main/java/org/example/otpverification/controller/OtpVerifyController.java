package org.example.otpverification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.otpverification.dto.GenerationReq;
import org.example.otpverification.dto.RequestDto;
import org.example.otpverification.dto.ResponseDto;
import org.example.otpverification.entity.Status;
import org.example.otpverification.service.OtpVerificationService;
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
    private final WebClient.Builder webClient;

    @PostMapping("/verify")
    public ResponseEntity<ResponseDto> verifyOtp(@Valid @RequestBody RequestDto request) {

        boolean isValid = otpVerificationService.verifyOtp(request.getEmail(), request.getOtp());

        if (isValid) {
            return ResponseEntity.ok(new ResponseDto(Status.ACTIVE.toString(), "otp deleted from database"));
        } else {
            return ResponseEntity.badRequest().body(new ResponseDto(Status.INACTIVE.toString(), "invalid otp"));
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestBody GenerationReq generationReq) {

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
