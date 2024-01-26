package com.mysite.sbb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.sbb.user.CustomAuthenticationFailureHandler;
import com.mysite.sbb.user.CustomAuthenticationSuccessHandler;

import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration //스프링의 환경설정 파일임을 의미하는 애너테이션
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 하는 애너테이션
public class SecurityConfig {
	
	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                )
            .csrf((csrf) -> csrf //모든 인증되지 않은 요청을 허락한, /h2-console/로 시작하는 URL은 CSRF검증을 하지 않는다는 설정을 추가
                    .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                    //.ignoringRequestMatchers(new AntPathRequestMatcher("/**"))
                    )
            .headers((headers) -> headers
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
				/*
				 * .contentSecurityPolicy(csp -> csp .policyDirectives("default-src 'self'; " +
				 * "style-src 'self' https://stackpath.bootstrapcdn.com; " +
				 * "script-src 'self'; " + "object-src 'none';"))
				 */
                    )
            .formLogin((formLogin) -> formLogin
                    .loginPage("/user/login")//로그인 URL
                    .successHandler(customAuthenticationSuccessHandler) // 로그인 성공 핸들러 설정
                    .failureHandler(customAuthenticationFailureHandler)// 로그인 실패 핸들러 설정 
                    //.defaultSuccessUrl("/question/list")//로그인 성공하면 리다이렉트 시켜서 로그인 성공 핸들러를 안 먹는거였네 하 
                    )
            .logout((logout) -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))// 로그아웃 URL
                    .logoutSuccessUrl("/")//로그아웃 성공시 리다이렉트 위치
                    .invalidateHttpSession(true)//로그아웃시 생성된 사용자 세션도 삭제
                    )
        ; 
	        			
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	//AuthenticationManager : 스프링 시큐리티의 인증을 담당합니다.
        return authenticationConfiguration.getAuthenticationManager();
    }
    




}
