package com.video.domain.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.video.domain.Video;
import com.video.domain.Video_;

public class VideoSpecification {

    public static Specification<Video> byTitlePhrase(String titlePhrase) {
        return (root, query, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.like(root.get(Video_.TITLE), "%" + titlePhrase + "%");
            return criteriaBuilder.and(p1);
        };
    }
}
