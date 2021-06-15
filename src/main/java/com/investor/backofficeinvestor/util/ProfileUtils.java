package com.investor.backofficeinvestor.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface ProfileUtils {
    String SCOPE = "SCOPE";
    String SCOPE_VALUE = System.getenv(SCOPE);

    static void calculateProfile() {
        String profile = Optional.ofNullable(SCOPE_VALUE)
                .filter(StringUtils::isNoneBlank)
                .orElseThrow(() -> new IllegalArgumentException("[Assertion Fail] SCOPE must be set to startup the application."));
        System.setProperty(SCOPE, profile);
    }
}
