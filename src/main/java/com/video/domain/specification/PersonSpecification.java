package com.video.domain.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.video.domain.Person;
import com.video.domain.Person_;

public class PersonSpecification {

    public static Specification<Person> byFullNamePhrase(String fullNamePhrase) {
        return (root, query, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.like(
                    criteriaBuilder.concat(root.get(Person_.FIRST_NAME), root.get(Person_.LAST_NAME)),
                    "%" + fullNamePhrase + "%");
            return criteriaBuilder.and(p1);
        };
    }
}
