package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);//제목으로 테이블의 데이터를 조회 ex) select * from table where subject=?
    Question findBySubjectAndContent(String subject, String content);//제목과 내용으로 테이블의 데이터를 조회
    List<Question> findBySubjectLike(String subject);//제목에 특정 문자열이 포함되어 있는 데이터를 조회
    Page<Question> findAll(Pageable pageable);//타입 객체를 리턴
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
    
    @Query("select "
            + "distinct q "
            + "from Question q " 
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    
    
}