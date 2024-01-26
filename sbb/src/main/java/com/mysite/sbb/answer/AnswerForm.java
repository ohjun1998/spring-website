package com.mysite.sbb.answer;

import com.mysite.sbb.secure.Secure;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm { //AnswerForm : 답변 등록하기 위한 폼
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
    
    public void setContent(String content) {//질문 제목 xss 공격 방지
    	this.content = Secure.xssSecure(content);
    }
}
