package com.investor.backofficeinvestor.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.investor.backofficeinvestor.model.ERole;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.model.Role;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.payload.request.LoginRequest;
import com.investor.backofficeinvestor.payload.request.SignupRequest;
import com.investor.backofficeinvestor.payload.response.JwtResponse;
import com.investor.backofficeinvestor.payload.response.MessageResponse;
import com.investor.backofficeinvestor.repository.RoleRepository;
import com.investor.backofficeinvestor.repository.UserRepository;
import com.investor.backofficeinvestor.security.jwt.JwtUtils;
import com.investor.backofficeinvestor.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.investor.backofficeinvestor.services.EmailService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    private final UserService userService;
    
    private final EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if (!userDetails.getActive()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("User inactive!");
            return ResponseEntity.ok(messageResponse);
        }


        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException, IOException {
        Optional<User> user = userRepository.findByEmail(signUpRequest.getEmail());

        Boolean existInactiveUser = user.isPresent() && !user.get().isActive();
        if (existInactiveUser) {
            try {
                Integer code = user.get().getValidationCode();
                String email = user.get().getEmail();
                emailService.sendEmail(email, "Su código de activación es:" + code.toString());
            } catch (Exception exception) {

            }

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(0);
            messageResponse.setMessage("User registered successfully!");
            return ResponseEntity.ok(messageResponse);
        }

        Boolean existUser = user.isPresent();
        if (existUser) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setStatus(1);
            messageResponse.setMessage("User already exist!");
            return ResponseEntity.ok(messageResponse);
        }

        // Create new user's account
        User newUser = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        newUser.setRoles(roles);
        userRepository.save(newUser);

        try {
            Integer code = newUser.getValidationCode();
            String email = newUser.getEmail();
            emailService.sendEmail(email, "Su código de activación es:" + code.toString());
        } catch (Exception exception) {

        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(0);
        messageResponse.setMessage("User registered successfully!");
        return ResponseEntity.ok(messageResponse);
    }
}