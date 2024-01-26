package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;

import java.util.Set;
import jakarta.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    
    @ManyToOne// 질문 N : 1 사용자, 여러개의 질문이 한 명의 사용자에게 작성될 수 있습니다.
    private SiteUser author;
    
    //질문, 답변의 수정 일시
    private LocalDateTime modifyDate;
    
    //질문에 추천한 사용자 컬럼 추가
    @ManyToMany
    Set<SiteUser> voter;//List가 아닌 Set인 이유? 하나의 질문에 추천인 중복은 안됩니다.
    
    private String filename;
    
    private String filepath;
    
    
    
}