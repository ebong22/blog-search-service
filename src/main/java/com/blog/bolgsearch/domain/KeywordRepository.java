package com.blog.bolgsearch.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findTop10ByOrderByCountDesc();
    Keyword findByKeyword(String query);
}
