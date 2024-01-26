package com.mysite.sbb.user;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mysite.sbb.question.QuestionForm;
import com.mysite.sbb.user.SiteUser;

import org.springframework.ui.Model;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")//회원가입 버튼클릭
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }
    
    @GetMapping("/profile")//프로필 버튼클릭
    public String profile(Model model, Principal principal) {
        String username = principal.getName();//현재 로그인한 계정이름
        SiteUser siteuser = this.userService.getUser(username);//userService에서 로그인한 사용자의 username에 해당하는 SiteUser 객체를 가져온다.
        model.addAttribute("siteuser", siteuser);//Model 객체에 siteuser 이름으로 SiteUser 객체를 추가한다.
        return "profile_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        
        try {//회원가입 중복 예외처리
        userService.create(userCreateForm.getUsername(), 
                userCreateForm.getEmail(), userCreateForm.getPassword1());
        }
        catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }
        
        catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        
        // 회원가입 성공하고 최상위 디렉터리로 리다이렉트
        return "redirect:/"; 
    }
    
    //로그인 버튼클릭
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
    
    //패스워드 변경 폼 으로 이동
    @GetMapping("/changePassword")
    public String changePassword() {
    	return "password_change";
    }
    
    //패스워드 변경 기능 수행
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal, RedirectAttributes redirectAttributes) {
    	
    	System.out.println("Changepassword");
        SiteUser siteUser = userService.getUser(principal.getName());

        // 비밀번호가 일치 여부 및 비밀번호 최소 길이
        if (!passwordEncoder.matches(currentPassword, siteUser.getPassword())) {
        	redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/user/changePassword";
        } else if(newPassword.length()<8) {
        	redirectAttributes.addFlashAttribute("error", "비밀번호 길이가 짧습니다.");
        	return "redirect:/user/changePassword";
        }

        // 비밀번호 변경 로직
        userService.changePassword(siteUser.getUsername(), newPassword);

        return "redirect:/user/profile";
    }

}
