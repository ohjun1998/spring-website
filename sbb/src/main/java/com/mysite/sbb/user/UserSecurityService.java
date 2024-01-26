package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
// UserSecurityService : 스프링 시큐리티 로그인 처리의 핵심 부분이다.
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//loadUserByUsername : 사용자명으로 비밀번호를 조회하여 리턴하는 메소드이다.
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        if (_siteUser.isEmpty()) {//사용자명에 해당하는 데이터가 있는지 체크
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get();
        
        //로그인 시도 5회 이상 실패시 잠김
        boolean accountNonLocked = true;
        if (siteUser.getFailedAttempt() >= 5) {
        	siteUser.setIsLocked(true);
            userRepository.save(siteUser);
            accountNonLocked = false;
        }
        

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
        	//사용자명이 'admin'이면 ADMIN 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
        	//사용자명이 'admin'이 아니면 USER 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가 화면으로부터 입력 받은 비밀번호와 일치하는지를 검사하는 로직을 내부적으로 가지고 있다.
        //return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
        return new User(siteUser.getUsername(), siteUser.getPassword(), 
        		true, true, true, accountNonLocked, authorities);
    }
}
