package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Set;
import jakarta.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;


    private LocalDateTime createDate;
    
    @ManyToOne
    private Question question;
    
    @ManyToOne// 질문 N : 1 사용자, 여러개의 질문이 한 명의 사용자에게 작성될 수 있습니다.
    private SiteUser author;
    
    //질문, 답변의 수정 일시
    private LocalDateTime modifyDate;
    
    //답변에 추천한 사용자 컬럼 추가
    @ManyToMany
    Set<SiteUser> voter;
}