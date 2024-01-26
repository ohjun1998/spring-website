package com.mysite.sbb.question;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.UUID;

import com.mysite.sbb.DataNotFoundException;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.question.QuestionFileUpload;

import com.mysite.sbb.answer.Answer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    //질문목록
    public List<Question> getList() {//getList():질문 목록을 조회하고 리턴
        return this.questionRepository.findAll();
    }
    
    //질문상세
    public Question getQuestion(Integer id) {//id값으로 question 데이터를 조회  
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
    
    //질문 등록
    /*public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }*/
    
    //질문 등록
    public void create(RedirectAttributes redirectAttributes, String subject, 
    					String content, SiteUser user, 
    					MultipartFile file) throws Exception {
    	
    	String fileName = QuestionFileUpload.fileUpload(redirectAttributes, file);
    	
    	Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        q.setFilename(fileName);
        q.setFilepath("/upload/"+fileName);
        this.questionRepository.save(q);
    }
    
    //질문 수정
    public void modify(RedirectAttributes redirectAttributes, Question question, 
    					String subject, String content, 
    					MultipartFile file, Model model) throws Exception{
    	
    	String fileName = QuestionFileUpload.fileUpload(redirectAttributes, file);
    	System.out.println("filename: "+fileName);
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        question.setFilename(fileName);
        question.setFilepath("/upload/"+fileName);
        this.questionRepository.save(question);
    }
    
    //질문 페이지 10개 지정, 작성일시 내림 차순, 질문 검색
    public Page<Question> getList(int page, String kw) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    
    //질문 삭제하는 delete 메소드
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    
    //추천인 저장
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
    
}
