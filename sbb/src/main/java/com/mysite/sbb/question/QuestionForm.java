package com.mysite.sbb.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.mysite.sbb.secure.Secure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm { //QuestionForm : 질문 등록하기 위한 폼
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;
    
    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
    
	/*
	 * public void setSubject(String subject) {//질문 제목 xss 공격 방지 this.subject =
	 * Secure.xssSecure(subject); }
	 * 
	 * public void setContent(String content) {//질문 내용 xss 공격 방지 this.content =
	 * Secure.xssSecure(content); }
	 */
    
    private String filename;
    
    private String filepath;
}
