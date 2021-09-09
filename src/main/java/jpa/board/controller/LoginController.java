package jpa.board.controller;

import jpa.board.common.Url;
import jpa.board.entity.UserAuthority;
import jpa.board.entity.Users;
import jpa.board.repository.UserAuthorityRepository;
import jpa.board.repository.UsersRepository;
import jpa.board.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    
    //로그인화면
    @GetMapping(value = {Url.AUTH.LOGIN})
    public String login(){
        return Url.AUTH.LOGIN_HTML;
    }

    //회원가입 화면
    @GetMapping(Url.AUTH.JOIN)
    public String join() {
        return Url.AUTH.JOIN_HTML;
    }

    //회원가입
    @ResponseBody
    @PostMapping(Url.AUTH.JOIN)
    public Map<String, Object> joinSubmit(@RequestBody Users users){
       
        //로그인 체크
        Map<String, Object> result = loginService.checkLoginInsert(users);
        return result;
    }

}
