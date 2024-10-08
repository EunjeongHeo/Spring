package com.example.demo.article.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // Builder 패턴으로 생성자를 대신함
public class Article {
    private Long id;
    private Board board;
    private String subject;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    public void update(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
