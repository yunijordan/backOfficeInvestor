package com.investor.backofficeinvestor.controller;

import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.services.dto.ResponseDTO;
import com.investor.backofficeinvestor.services.dto.ValidateDTO;
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

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/validate/")
    public ResponseEntity<ResponseDTO> validateOTP(@Valid @RequestBody ValidateDTO validateDTO) {
        String email = validateDTO.getEmail();
        Integer code = validateDTO.getCode();
        Optional<User> result = userService.findByValidationCode(code, email);
        if (result.isPresent()) {
            User user = result.get();
            user.setActive(true);
            userService.save(user);
            return ResponseEntity.status(200).body(new ResponseDTO(true));
        }
        return ResponseEntity.status(200).body(new ResponseDTO(false));
    }
}
