package com.investor.backofficeinvestor;

import com.investor.backofficeinvestor.util.ProfileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackOfficeInvestorApplication {

    public static void main(String[] args) {
//        ProfileUtils.calculateProfile();
        SpringApplication.run(BackOfficeInvestorApplication.class, args);
    }

}
