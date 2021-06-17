package com.investor.backofficeinvestor.controller;

import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.services.dto.MessageResponseDTO;
import com.investor.backofficeinvestor.services.dto.ResponseDTO;
import com.investor.backofficeinvestor.services.dto.ValidateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
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

    @Operation(summary = "Get a user by Id" )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@Parameter(description = "id of user to be searched") @PathVariable(value = "id") Long userId) {
        User result = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", userId)));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get a user by Email" )
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@Parameter(description = "email of user to be searched") @PathVariable(value = "email") String email) {
        User result = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", email)));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate/")
    public ResponseEntity<ResponseDTO> validateOTP(@Valid @RequestBody ValidateDTO validateDTO){
        String email = validateDTO.getEmail();
        Integer code = validateDTO.getCode();
//        ResponseDTO responseDTO = new ResponseDTO(true);
        Optional<User> result = userService.findByValidationCode(code,email);
//        .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", code)));
        if (result.isPresent()){
            result.get().setActive(true);
//            result.ifPresent(()-> );
//            return ResponseEntity.status(200).body("Code Valid");
            return ResponseEntity.status(200).body(new ResponseDTO(true));
//            responseDTO.setStatus(true);
//            return responseDTO;
        }
        return ResponseEntity.status(200).body(new ResponseDTO(false));
//        return ResponseEntity.status(200).body("Code Invalid");
//        return ResponseEntity.status(200).body(new MessageResponseDTO("Code valido"));
    }
}
