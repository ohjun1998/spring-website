package com.mysite.sbb.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;


import org.springframework.validation.BindingResult;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.secure.Secure;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

	private final QuestionService questionService;;
	private final UserService userService;
	
    @GetMapping("/list") //질문 검색
    public String list(Model model, RedirectAttributes redirectAttributes, 
    @RequestParam(value="page", defaultValue="0") int page, 
    @RequestParam(value = "kw", defaultValue = "") String kw) {
    	if(kw.length() > 20){
    		// redirect시에도 데이터를 유지할 수 있도록 RedirectAttributes 사용, Model 사용하면 redirect 할때 새로운 Model 객체가 생성되서 error 속성값이 안들어감
    		redirectAttributes.addFlashAttribute("error", "검색어는 20자를 초과할 수 없습니다.");
            return "redirect:/question/list";
        }
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        //list 메소드에서 조회한 질문 목록을 'questionList' 이름으로 Model 객체에 저장한다.
        return "question_list";
    }
    
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, 
    					@PathVariable("id") Integer id, 
    					AnswerForm answerForm) {
    	
        Question question = this.questionService.getQuestion(id);
        //QuestionController에서 QuestionService의 getQuestion 메서드를 호출하여 Question 객체를 템플릿에 전달
        model.addAttribute("question", question);
        return "question_detail";
    }
    
    /*@GetMapping("/list")//메인 홈 화면으로 이동
    public String list() {
        return "question_list";
    }*/
    
    //질문 폼으로 이동
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
    	return "question_form";
    }

    
    //질문 등록
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, 
    									Principal principal, MultipartFile file, 
    									RedirectAttributes redirectAttributes) throws Exception {
    	
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        
        SiteUser siteUser = this.userService.getUser(principal.getName());
        
        try {
        	this.questionService.create(redirectAttributes, questionForm.getSubject(),
        								questionForm.getContent(), siteUser, file);
        } 
        catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
        	return "redirect:/question/create";
        }
        
        return "redirect:/question/list";
    }
    
    //질문 수정 클릭
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, 
    							 Principal principal, MultipartFile file) throws Exception {
    	
    	//String fileName = QuestionFileUpload.fileUpload(file);
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        //작성자가 일치한 경우 질문의 제목과 내용을 가져옵니다.
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        //questionForm.setFilename(fileName);
        //questionForm.setFilepath("/upload/"+fileName);
        //questionForm.setFilename(question.getFilename());
        //questionForm.setFilepath(question.getFilepath());
        return "question_form";
    }
    
    //질문 수정 완료
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
    							 Principal principal, @PathVariable("id") Integer id, 
    							 MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws Exception{
    	
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        //현재 로그인되어있는 사용자와 질문의 작성자가 동일한지 확인
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        //질문 수정 호출
        try {
        	this.questionService.modify(redirectAttributes, question, questionForm.getSubject(), 
        								questionForm.getContent(), file, model);
        } catch (Exception e) {
        	model.addAttribute("error", e.getMessage());
        	return "question_form";
        }
        
        return String.format("redirect:/question/detail/%s", id);
    }
    
    //질문 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
    
    //추천인 버튼 눌렀을 때
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
    
    //파일 다운받기
    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename, 
    														HttpServletRequest request) throws IOException {
    	
    	//파일 다운로드 취약점 대응
    	Secure.filedownloadSecure(filename);
    	
        ServletContext servletContext = request.getServletContext();
        InputStream inputStream = servletContext.getResourceAsStream("/upload/" + filename);
        
        if (inputStream == null) {
            throw new IOException("File not found.");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename="+filename) // 다운로드시 저장될 파일명
                .contentType(MediaType.parseMediaType("application/octet-stream")) // 파일 종류에 따라 적절한 MIME 타입을 설정하세요.
                .body(new InputStreamResource(inputStream));
    }
}
