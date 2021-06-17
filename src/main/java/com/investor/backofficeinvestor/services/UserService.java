package com.investor.backofficeinvestor.services;


import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserService.class);

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

//    public User updateUser(User user) {
//        User bdUser = userRepository.findById(user.getId())
//                .map(dbUser -> {
//                    if (StringUtils.isNotBlank(user.getEmail())) {
//                        dbUser.setEmail(user.getEmail());
//                    }
//                    if (StringUtils.isNotBlank(user.getFullName())) {
//                        dbUser.setFullName(user.getFullName());
//                    }
//                    if (StringUtils.isNotBlank(user.getOperatorId())) {
//                        dbUser.setOperatorId(user.getOperatorId());
//                    }
//                    return dbUser;
//                })
//                .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", user.getId())));
//
//        return userRepository.save(bdUser);
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<User> findByUsername(String username) {
//        return userRepository.findOne((root, query, builder) -> builder.equal(root.get("username"), username));
//    }
//
//    public User signUp(SignupRequestDTO signUpRequestDTO, User authenticatedUser) {
//        if (userRepository.existsByUsername(signUpRequestDTO.getUsername())) {
//            throw new ApiException("in_use", "Error: Username is already taken!", HttpStatus.BAD_REQUEST
//                    .value());
//        }
//
//        if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
//            throw new ApiException("in_use", "Error: Email is already in use!", HttpStatus.BAD_REQUEST
//                    .value());
//        }
//
//        if (userRepository.existsByOperatorId(signUpRequestDTO.getOperatorid())) {
//            throw new ApiException("in_use", "Error: OperatorId is already in use!", HttpStatus.BAD_REQUEST
//                    .value());
//        }
//
//        RoleType maxRoleAuth = roleService.getMaxRole(authenticatedUser.getRoles());
//        Issuer issuer = null;
//        if (signUpRequestDTO.getIssuerId() != null) {
//            issuer = issuerRepository.findById(signUpRequestDTO.getIssuerId())
//                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Issuer: %s not found", signUpRequestDTO.getIssuerId())));
//
//            if (!issuer.isActive()) {
//                throw new ResourceNotFoundException("Issuer not found");
//            }
//        }
//
//        Set<Role> userRoles = Optional.ofNullable(signUpRequestDTO.getRole())
//                .orElseGet(HashSet::new)
//                .stream()
//                .map(roleStr -> {
//                    final RoleType roleType;
//                    switch (roleStr) {
//                        case "admin":
//                            roleType = RoleType.ROLE_ADMINISTRADOR;
//                            break;
//                        case "rep":
//                            roleType = RoleType.ROLE_REPRESENTANTE;
//                            break;
//                        case "veritran":
//                            roleType = RoleType.ROLE_VERITRAN;
//                            break;
//                        default:
//                            roleType = null;
//                    }
//
//                    if (roleType != null) {
//                        return roleService.findByName(roleType)
//                                .orElseThrow(() -> new ResourceNotFoundException(String.format("Error: Role: %s not found", roleType.name())));
//                    }
//                    return null;
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet());
//
//        RoleType maxRole = roleService.getMaxRole(userRoles);
//        if ((maxRole.equals(RoleType.ROLE_ADMINISTRADOR) || maxRole.equals(RoleType.ROLE_REPRESENTANTE))
//                && signUpRequestDTO.getIssuerId() == null) {
//            throw new ResourceNotFoundException(String.format("Error: issuerID is required for %s", maxRole.name()));
//        }
//        if (maxRole.equals(RoleType.ROLE_VERITRAN)) {
//            issuer = null;
//        }
//
//        Set<Role> authenticatedUserRoles = authenticatedUser.getRoles();
//        RoleType authenticatedUserMaxRole = roleService.getMaxRole(authenticatedUserRoles);
//
//        if (authenticatedUserMaxRole.equals(RoleType.ROLE_VERITRAN) &&
//                !(signUpRequestDTO.getRole().contains("admin") || signUpRequestDTO.getRole().contains("veritran"))) {
//            throw new ApiException("wrong_permissions", "Wrong permissions", 400);
//        }
//
//        if (authenticatedUserMaxRole.equals(RoleType.ROLE_ADMINISTRADOR)) {
//            if (!signUpRequestDTO.getRole().contains("admin") && !signUpRequestDTO.getRole().contains("rep")) {
//                throw new ApiException("wrong_permissions",
//                        "An Administrator user can only add users with Admin and Representante roles", 400);
//            }
//
//            if (!signUpRequestDTO.getIssuerId().equals(authenticatedUser.getIssuer().getId())) {
//                throw new ApiException("wrong_permissions",
//                        "An Administrator user only can add users within it's issuer", 400);
//            }
//        }
//
//        // Create new user's account
//        User user = new User(signUpRequestDTO.getUsername(),
//                signUpRequestDTO.getEmail(),
//                signUpRequestDTO.getPassword(),
//                signUpRequestDTO.getFullname(),
//                signUpRequestDTO.getOperatorid(),
//                issuer,
//                true,
//                null);
//
//        user.setRoles(userRoles);
//
//        return userRepository.save(user);
//    }
//
//    public void deleteById(Long userId) {
//        userRepository.findById(userId)
//            .ifPresent(user -> {
//                user.setDeletedAt(new Date());
//                userRepository.save(user);
//            });
//    }
//
//    public void updateUserState(Long userId, UserSummaryDTO user) {
//        if (user.isActive() != null){
//            userRepository.findById(userId)
//                    .ifPresent(userFound -> {
//                        userFound.setActive(user.isActive());
//                        userRepository.save(userFound);
//                    });
//        }
//    }
//
//    public void resetPassword(Long userId, PasswordDTO passwordsDTO, User authenticatedUser, Boolean force) {
//        boolean passwordChanged = applyUserChangesPasswordStrategy(userId, passwordsDTO, force)
//                || applyVeritranChangePasswordStrategy(userId, passwordsDTO, authenticatedUser, force)
//                || applyAdministratorChangePasswordStrategy(userId, passwordsDTO, authenticatedUser, force);
//
//        if (!passwordChanged) {
//            throw new ApiException("wrong_permissions", "Wrong Permissions", HttpStatus.UNAUTHORIZED.value());
//        }
//    }
//
//    private boolean applyUserChangesPasswordStrategy(Long userId, PasswordDTO passwordsDTO, Boolean force) {
//        if (!force) {
//            char[] currentPassword = passwordsDTO.getCurrentPassword();
//            char[] candidatePassword = passwordsDTO.getNewPassword();
//
//            User dbUser = userRepository.findById(userId)
//                    .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", userId)));
//
//            if (!passwordEncoder.matches(new String(currentPassword), dbUser.getPassword())) {
//                throw new ApiException("wrong_password", "Wrong password", HttpStatus.UNAUTHORIZED
//                        .value());
//            }
//
//            if (Arrays.equals(currentPassword, candidatePassword)) {
//                throw new ApiException("same_password", "The new password must be different from the current one", HttpStatus.UNAUTHORIZED
//                        .value());
//            }
//
//            dbUser.setPassword(passwordEncoder.encode(new String(candidatePassword)));
//            userRepository.save(dbUser);
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean applyVeritranChangePasswordStrategy(Long userId, PasswordDTO passwordsDTO, User authenticatedUser, Boolean force) {
//        if (force) {
//            User dbUser = userRepository.findById(userId)
//                    .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", userId)));
//
//            RoleType maxRoleAuth = roleService.getMaxRole(authenticatedUser.getRoles());
//            RoleType maxRoleSubject = roleService.getMaxRole(dbUser.getRoles());
//
//            //  An user with Veritran role can change the password to another user with Veritran o Administrator role
//            if (maxRoleAuth.equals(RoleType.ROLE_VERITRAN)
//                    && (maxRoleSubject.equals(RoleType.ROLE_VERITRAN) || maxRoleSubject.equals(RoleType.ROLE_ADMINISTRADOR))) {
//                dbUser.setPassword(passwordEncoder.encode(new String(passwordsDTO.getNewPassword())));
//                userRepository.save(dbUser);
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private boolean applyAdministratorChangePasswordStrategy(Long userId, PasswordDTO passwordsDTO, User authenticatedUser, Boolean force) {
//        if (force) {
//            User dbUser = userRepository.findById(userId)
//                    .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", userId)));
//
//            RoleType maxRoleAuth = roleService.getMaxRole(authenticatedUser.getRoles());
//
//            //  An user with Administrator role can change the password to anyone within it's issuer
//            Issuer authIssuer = authenticatedUser.getIssuer();
//            Issuer subjectIssuer = dbUser.getIssuer();
//            if (maxRoleAuth.equals(RoleType.ROLE_ADMINISTRADOR)
//                    && subjectIssuer != null
//                    && authIssuer.getId().equals(subjectIssuer.getId())) {
//                dbUser.setPassword(passwordEncoder.encode(new String(passwordsDTO.getNewPassword())));
//                userRepository.save(dbUser);
//                return true;
//            }
//        }
//
//        return false;
//    }
}
