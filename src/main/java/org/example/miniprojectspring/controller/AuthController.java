package org.example.miniprojectspring.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.miniprojectspring.exception.OTPExpiredException;
import org.example.miniprojectspring.exception.PasswordException;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.request.AppUserRequest;
import org.example.miniprojectspring.model.request.AuthRequest;
import org.example.miniprojectspring.model.request.PasswordRequest;
import org.example.miniprojectspring.model.response.ApiResponse;
import org.example.miniprojectspring.model.response.AuthResponse;
import org.example.miniprojectspring.security.JwtService;
import org.example.miniprojectspring.service.AppUserService;
import org.example.miniprojectspring.service.EmailService;
import org.example.miniprojectspring.service.OptGenerator;
import org.example.miniprojectspring.service.OptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@RequestMapping("/api/v1/auths")
@RestController
@AllArgsConstructor
public class AuthController {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final EmailService emailService;
    private final OptService optService;


    @PutMapping("/verify")
    public ResponseEntity<?> verify(String OptCode) {
        Optional<OptsDTO> optional = optService.findByCode(OptCode);
        optional.ifPresent(c -> {
            if (c.getExpiration().before(new Date())) {
                try {
                    throw new OTPExpiredException("Opt is expired");
                } catch (OTPExpiredException e) {
                    throw new RuntimeException(e);
                }
            } else {
                optService.verify(OptCode);
            }
        });
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .code(201)
                .message("Your email is verified")
                .payload("Successfully verified")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/forget")
    public ResponseEntity<?> forget(@RequestBody PasswordRequest passwordRequest, String email) throws PasswordException {
        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
            throw new PasswordException("Your password is not matched");
        }
        passwordRequest.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        optService.resetPasswordByEmail(passwordRequest, email);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().payload("Password Reseted").message("Password resetted successfully").code(201).status(HttpStatus.OK).build();
        return ResponseEntity.ok(apiResponse);

    }

    @PostMapping("resend")
    public ResponseEntity<?> resend(@RequestBody PasswordRequest passwordRequest) throws PasswordException {

        return ResponseEntity.ok("sdf");
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody AppUserRequest appUserRequest) throws MessagingException, IOException {
        String encodedPassword = passwordEncoder.encode(appUserRequest.getPassword());
        appUserRequest.setPassword(encodedPassword);
        AppUserDTO appUserDTO = appUserService.createUser(appUserRequest);
        System.out.println(appUserDTO);
        OptsDTO optsDTO = OptGenerator.generateOTP(6, appUserDTO.getUserId());
        optService.save(optsDTO);
        String optsGenerated = emailService.sendEmail(appUserRequest.getEmail(), optsDTO.getOptCode());
        System.out.println(optsGenerated);
        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .message("Successfully Registered")
                .code(201)
                .status(HttpStatus.CREATED)
                .payload(appUserDTO)
                .build();
        System.out.println(appUserDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws Exception {

        authenticate(authRequest.getEmail(), authRequest.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(token);

        return ResponseEntity.ok(authResponse);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            UserDetails userDetails = appUserService.loadUserByUsername(email);
//            System.out.println("UserDetail : " + userDetails);
            if (userDetails == null) {
                throw new BadRequestException("User not found");
            }

            System.out.println(passwordEncoder.matches(password, userDetails.getPassword()));
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }


}
