package com.mysite.sbb.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //계정 생성
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
    
    //계정 정보 가져오기
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
    
    //패스워드 변경
    public void changePassword(String username, String newPassword) {
    	
        Optional<SiteUser> optionalSiteUser = this.userRepository.findByusername(username);
        
        if (optionalSiteUser.isPresent()) {
            SiteUser siteUser = optionalSiteUser.get();  // SiteUser 객체를 가져옵니다.
            String encodedPassword = passwordEncoder.encode(newPassword);
            siteUser.setPassword(encodedPassword);  // SiteUser 객체의 비밀번호를 변경합니다.
            userRepository.save(siteUser);  // 변경된 SiteUser 객체를 저장합니다.
        } else {
        	throw new DataNotFoundException("siteuser not found");
        }
    }

}
