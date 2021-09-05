package jpa.board.controller;

import jpa.board.common.Url;
import jpa.board.entity.Users;
import jpa.board.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    UsersRepository usersRepository;

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
    public Users joinSubmit(@RequestBody Users users){
        log.info("users={}", users.toString());
        usersRepository.save(users);
        return users;
    }

}
