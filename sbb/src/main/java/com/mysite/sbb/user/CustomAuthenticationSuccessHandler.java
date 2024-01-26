package com.mysite.sbb.user;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import ch.qos.logback.core.model.Model;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	
    private UserRepository userRepository;
    
	@Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    									HttpServletResponse response,
                                        Authentication authentication) 
                                        		throws IOException, ServletException {
    	
    	System.out.println("onAuthenticationSuccess");

        String username = authentication.getName();
        System.out.println("username: "+username);
        Optional<SiteUser> _siteUser = this.userRepository .findByusername(username);
        
        if (_siteUser.isPresent()) {
            SiteUser siteUser = _siteUser.get();
            siteUser.setFailedAttempt(0); // 로그인 성공 시 실패 횟수를 0으로 리셋
            userRepository.save(siteUser);
        }
        
        response.sendRedirect("/question/list"); // 로그인 성공 후 리다이렉트할 URL
    }
        
}

