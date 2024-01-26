package com.mysite.sbb.user;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//로그인 실패 했을 경우 핸들러
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Autowired
    private UserRepository userRepository;
    

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
    									HttpServletResponse response, 
    									AuthenticationException exception) 
    											throws IOException, ServletException {
    	
    	System.out.println("onAuthenticationFailure");
    	
    	String username = request.getParameter("username");
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        
        //로그인 실패할 경우 FailedAttempt + 1
        if (_siteUser.isPresent()) {  
        	SiteUser siteUser = _siteUser.get();
        	siteUser.setFailedAttempt(siteUser.getFailedAttempt() + 1);
            userRepository.save(siteUser);
        }
        
        String errorMessage;
        
        // 예외처리  
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        } else if (exception instanceof LockedException) {
            errorMessage = "계정이 만료되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
        }
		  
        response.sendRedirect("/user/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }
}
