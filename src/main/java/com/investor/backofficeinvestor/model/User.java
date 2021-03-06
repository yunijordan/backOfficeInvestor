package com.investor.backofficeinvestor.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User implements UserDetails{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(max = 20)
        private String username;

        @NotBlank
        @Size(max = 50)
        @Email
        private String email;

        @NotBlank
        @Size(max = 120)
        private String password;

        @Column(name = "active")
        private boolean active = false;

        @Column(name = "validation_code")
        private Integer validationCode = (int) Math.floor(Math.random() * (999999-100000+1)) + 100000;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(	name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles = new HashSet<>();

        @OneToMany(mappedBy = "user")
        private Set<Payment> payments;

        public User() {
        }

        public User(String username, String email, String password) {
                this.username = username;
                this.email = email;
                this.password = password;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return new ArrayList(roles);
        }


//        @Override
//        public boolean isAccountNonExpired() {
//                return true;
//        }
//
//        @Override
//        public boolean isAccountNonLocked() {
//                return true;
//        }
//
//        @Override
//        public boolean isCredentialsNonExpired() {
//                return true;
//        }
//
//        @Override
//        public boolean isEnabled() {
//                return true;
//        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//                return new ArrayList(roles);
//        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public Set<Role> getRoles() {
                return roles;
        }

        public void setRoles(Set<Role> roles) {
                this.roles = roles;
        }

        public boolean isActive() {
                return active;
        }

        public void setActive(boolean active) {
                this.active = active;
        }

        public Integer getValidationCode() {
                return validationCode;
        }

        public void setValidationCode(Integer validationCode) {
                this.validationCode = validationCode;
        }

        public Set<Payment> getPayments() {
                return payments;
        }

        public void setPayments(Set<Payment> payments) {
                this.payments = payments;
        }
}