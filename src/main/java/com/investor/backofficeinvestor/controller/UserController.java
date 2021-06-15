package com.investor.backofficeinvestor.controller;

import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.services.UserService;
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

}
