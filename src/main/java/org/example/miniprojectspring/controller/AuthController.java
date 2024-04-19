package org.example.miniprojectspring.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.miniprojectspring.exception.CustomNotFoundException;
import org.example.miniprojectspring.exception.PasswordException;
import org.example.miniprojectspring.exception.SearchNotFoundException;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.dto.OptsDTO;
import org.example.miniprojectspring.model.entity.CustomUserDetail;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
        OptsDTO optsDTO = optService.findByCode(OptCode);
        ApiResponse<String> apiResponse = null;
        if (optsDTO != null) {
            apiResponse = ApiResponse.<String>builder()
                    .status(HttpStatus.OK)
                    .code(201)
                    .localDateTime(LocalDateTime.now())
                    .message("Your opt has been sent to your email")
                    .payload("Successfully verified")
                    .build();
            optService.verify(OptCode);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/forget")
    public ResponseEntity<?> forget(@RequestBody PasswordRequest passwordRequest, String email) {

        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
            throw new PasswordException("password is not matched");
        }
        AppUserDTO userDTO = appUserService.findUserByEmail(email);
        if(userDTO== null){
            throw new SearchNotFoundException("Email : " + email + " not found");
        }
        passwordRequest.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        optService.resetPasswordByEmail(passwordRequest, email);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().payload("Password Reset").message("Password Reset successfully").code(201).status(HttpStatus.OK).build();
        return ResponseEntity.ok(apiResponse);

    }

    @PostMapping("resend")
    public ResponseEntity<?> resend(String email) throws MessagingException, IOException {
        optService.resendOpt(email);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().message("The opt has been resetted to the email. Please check your mail").code(201).status(HttpStatus.OK).payload("Sent to the email").build();
        return ResponseEntity.ok(apiResponse);
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
                .localDateTime(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .payload(appUserDTO)
                .build();
        System.out.println(appUserDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws Exception {

        AppUserDTO appUserDTO = appUserService.findUserByEmail(authRequest.getEmail());
        if(appUserDTO == null){
            throw new SearchNotFoundException("Email : " + authRequest.getEmail() + " not found");
        }
        System.out.println(appUserDTO + " first in login");
        OptsDTO optsDTO = optService.findOptByUserId(appUserDTO.getUserId());
        System.out.println(optsDTO + " is verify");
        if (optsDTO.isVerify()) {
            System.out.println(optsDTO.isVerify());
            authenticate(authRequest.getEmail(), authRequest.getPassword());
            final UserDetails userDetails = appUserService.loadUserByUsername(authRequest.getEmail());
            final String token = jwtService.generateToken(userDetails);
            AuthResponse authResponse = new AuthResponse(token);

            return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.ok("Your email is not verified");
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
