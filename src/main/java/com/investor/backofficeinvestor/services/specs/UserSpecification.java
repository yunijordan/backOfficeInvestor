package com.investor.backofficeinvestor.services.specs;

import com.investor.backofficeinvestor.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class UserSpecification {
//    public static Specification<User> userIsActive() {
//        return (root, query, builder) -> builder.isTrue(root.join("issuer", JoinType.LEFT).get("active"));
//    }
//
//    public static Specification<User> issuerIsNull() {
//        return (root, query, builder) -> builder.isNull(root.get("issuer"));
//    }
}
