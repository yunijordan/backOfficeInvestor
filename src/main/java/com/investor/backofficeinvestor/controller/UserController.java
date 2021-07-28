package com.investor.backofficeinvestor.controller;

import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.payload.request.SignupRequest;
import com.investor.backofficeinvestor.payload.response.MessageResponse;
import com.investor.backofficeinvestor.repository.UserRepository;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.services.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<User> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Operation(summary = "Get a user by Id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@Parameter(description = "id of user to be searched") @PathVariable(value = "id") Long userId) {
        User result = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", userId)));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get a user by Email")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@Parameter(description = "email of user to be searched") @PathVariable(value = "email") String email) {
        User result = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", email)));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOTP(@Valid @RequestBody ValidateDTO validateDTO) {

        Optional<User> user = userRepository.findByEmail(validateDTO.getEmail());
        if (!user.isPresent()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("The user doesnt exist!");
            return ResponseEntity.ok(messageResponse);
        }

        if (!user.get().getValidationCode().equals(validateDTO.getCode())) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("Invalid code!");
            return ResponseEntity.ok(messageResponse);
        }

        user.get().setActive(true);
        userService.save(user.get());
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(0);
        messageResponse.setMessage("User validated successful!");
        return ResponseEntity.ok(messageResponse);
    }

    @PatchMapping("/change_password/{email}")
    public ResponseEntity<?> changePassword(@Parameter(description = "email of user to be searched") @PathVariable(value = "email") String email,
                                            @RequestParam(value = "force") Boolean force, @Valid @RequestBody PasswordDTO passwords
    ) {

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("The user doesnt exist!");
            return ResponseEntity.ok(messageResponse);
        }

        boolean passwordChanged = userService.resetPassword(email, passwords, force);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(passwordChanged ? 0 : 1);
        messageResponse.setMessage(passwordChanged ? "Password changed success!" : "The password is incorrect!");
        return ResponseEntity.ok(messageResponse);
    }

    @PatchMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {

        Optional<User> user = userRepository.findByEmail(resetPasswordDTO.getEmail());
        if (!user.isPresent()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("The user doesnt exist!");
            return ResponseEntity.ok(messageResponse);
        }

        if (!user.get().getValidationCode().equals(resetPasswordDTO.getPin())) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("Invalid pin!");
            return ResponseEntity.ok(messageResponse);
        }

        boolean passwordChanged = userService.changesPassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getPassword());

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(passwordChanged ? 0 : 1);
        messageResponse.setMessage(passwordChanged ? "Password changed success!" : "Unknow error!");
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody SignupRequest signupRequest) {
        Optional<User> user = userRepository.findByEmail(signupRequest.getEmail());

        if (user.isPresent()) {
            try {
                Integer code = (int) Math.floor(Math.random() * (999999 - 100000 + 1)) + 100000;
                user.get().setValidationCode(code);
                userService.save(user.get());
//                userService.sendmail(signupRequest.getEmail(), "Your password has changed. Your new activaction code is:" + code.toString());
            } catch (Exception exception) {

            }

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(0);
            messageResponse.setMessage("Mail sent!");
            return ResponseEntity.ok(messageResponse);
        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(1);
        messageResponse.setMessage("The user doesnt exist!");
        return ResponseEntity.ok(messageResponse);
    }
}
