package com.investor.backofficeinvestor.services;


import com.investor.backofficeinvestor.exceptions.ApiException;
import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.repository.UserRepository;
import com.investor.backofficeinvestor.services.dto.PasswordDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

@Service
@Transactional
public class UserService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Value("${investor.mail.smtp.auth}")
    private  String auth;
    @Value("${investor.mail.smtp.starttls.enable}")
    private String starttls;
    @Value("${investor.mail.smtp.host}")
    private String host;
    @Value("${investor.mail.smtp.port}")
    private String port;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    public Page<User> findAll( Pageable pageable) {
            return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByValidationCode(Integer code, String email) {
        return userRepository.findByValidationCodeAndEmail(code, email);
    }
    public User updateUser(User user) {
        User bdUser = userRepository.findByEmail(user.getEmail())
                .map(dbUser -> {
                    if (user.isActive() == false ) {
                        dbUser.setActive(true);
                    }
                    return dbUser;
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", user.getEmail())));

        return userRepository.save(bdUser);
    }

    public User save(User user){
        return userRepository.save(user);

    }
    public void sendmail(String addressDestiny , String context) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("garciayunnier@gmail.com", "Geraldine*2021");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("garciayunnier@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressDestiny));
        msg.setSubject("Probando suscription");
        msg.setContent("Prueba", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(context, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
//            MimeBodyPart attachPart = new MimeBodyPart();
//
//            attachPart.attachFile("/var/tmp/image19.png");
//            multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void resetPassword(Long userId, PasswordDTO passwordsDTO, Object authenticatedUser, Boolean force) {
        boolean passwordChanged = applyUserChangesPasswordStrategy(userId, passwordsDTO,authenticatedUser,force);

        if (!passwordChanged) {
            throw new ApiException("wrong_permissions", "Wrong Permissions", HttpStatus.UNAUTHORIZED.value());
        }
    }

    private boolean applyUserChangesPasswordStrategy(Long userId, PasswordDTO passwordsDTO,Object authenticatedUser, Boolean force) {
        if (!force) {
            char[] currentPassword = passwordsDTO.getCurrentPassword();
            char[] candidatePassword = passwordsDTO.getNewPassword();

            User dbUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", userId)));

            if (!passwordEncoder.matches(new String(currentPassword), dbUser.getPassword())) {
                throw new ApiException("wrong_password", "Wrong password", HttpStatus.UNAUTHORIZED
                        .value());
            }

            if (Arrays.equals(currentPassword, candidatePassword)) {
                throw new ApiException("same_password", "The new password must be different from the current one", HttpStatus.UNAUTHORIZED
                        .value());
            }

            dbUser.setPassword(passwordEncoder.encode(new String(candidatePassword)));
            userRepository.save(dbUser);
            return true;
        }

        return false;
    }
}
