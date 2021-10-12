package jpa.board.controller;

import jpa.board.common.Url;
import jpa.board.entity.Users;
import jpa.board.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

/**
 * 로그인 컨트롤러
 */

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * 로그인 화면
     * @return
     */
    @GetMapping(value = {Url.AUTH.LOGIN})
    public String login(){
        return Url.AUTH.LOGIN_HTML;
    }

    /**
     * 회원가입 화면
     * @return
     */
    @GetMapping(Url.AUTH.JOIN)
    public String join() {
        return Url.AUTH.JOIN_HTML;
    }

    /**
     * 회원가입
     * @param users
     * @return
     */
    @ResponseBody
    @PostMapping(Url.AUTH.JOIN)
    public Map<String, Object> joinSubmit(@RequestBody Users users){
       
        //로그인 체크
        return loginService.checkLoginInsert(users);
    }

}
