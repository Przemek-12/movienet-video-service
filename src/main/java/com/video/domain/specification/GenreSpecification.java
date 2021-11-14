package com.video.domain.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.video.domain.Genre;
import com.video.domain.Genre_;

public class GenreSpecification {

    public static Specification<Genre> byNamePhrase(String namePhrase) {
        return (root, query, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.like(root.get(Genre_.NAME), "%" + namePhrase + "%");
            return criteriaBuilder.and(p1);
        };
    }
}
