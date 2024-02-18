package com.backend.seabook.controller;

import com.backend.seabook.dto.request.LoginRequest;
import com.backend.seabook.dto.request.RegisterRequest;
import com.backend.seabook.dto.response.LoginResponse;
import com.backend.seabook.dto.response.RefreshTokenResponse;
import com.backend.seabook.dto.response.RegisterResponse;
import com.backend.seabook.dto.response.base.APIResponse;
import com.backend.seabook.dto.response.base.APIResultResponse;
import com.backend.seabook.service.AuthenticationService;
import com.backend.seabook.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.seabook.common.util.Constants.AuthPats.AUTH_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_PATS, produces = "application/json")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<APIResultResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authenticationService.register(request);
        APIResultResponse<RegisterResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Register success",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResultResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authenticationService.login(request);
        APIResultResponse<LoginResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Login success",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/email-verify-register")
    public ResponseEntity<APIResponse> verifyEmailRegister(@RequestParam String token) {
        emailService.verifyEmailRegister(token);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email successfully verified"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<APIResultResponse<RefreshTokenResponse>> refreshToken() {
        RefreshTokenResponse response = authenticationService.refreshToken();
        APIResultResponse<RefreshTokenResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Refresh token success",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }
}
