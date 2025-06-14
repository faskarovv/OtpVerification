package org.example.otpverification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.otpverification.entity.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
}
