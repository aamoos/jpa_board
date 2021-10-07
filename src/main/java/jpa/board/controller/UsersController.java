package jpa.board.controller;

import jpa.board.entity.Board;
import jpa.board.entity.UserAuthority;
import jpa.board.entity.Users;
import jpa.board.repository.UserAuthorityRepository;
import jpa.board.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  사용자 관리 controller
 */

@Controller
@Slf4j
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthorityRepository userAuthorityRepository;

    @GetMapping("/users/list")
    public String list(@RequestParam(required = false, defaultValue = "0", value = "page") int page
                     , @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword
                     , Model model){

        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Users> listPage =  usersService.list(page, keyword);

        //총페이지수
        int totalPage = listPage.getTotalPages();
        model.addAttribute("users", listPage.getContent());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageNo", page);
        model.addAttribute("resultDataTotal", listPage.getTotalElements());
        model.addAttribute("size", listPage.getSize());
        model.addAttribute("number", listPage.getNumber());
        model.addAttribute("keyword", keyword);

        return "users/list";
    }

    @GetMapping("/users/write")
    public String write(){
        return "users/write";
    }

    @ResponseBody
    @PostMapping("/users/write")
    public Long writeSubmit(@RequestBody Users users){
        log.info("fileIdxs={}", users.getFileIdxs());
        String fileIdx = users.getFileIdxs();
        if(!isStringEmpty(users.getFileIdxs())){
            fileIdx = ((String) users.getFileIdxs()).replace("[", "").replace("]", "");
            users.setFileIdx(Long.parseLong(fileIdx));
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));

        //board 게시판 테이블 insert
        Long userIdx = usersService.savePost(users);

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthIdx(users.getAuthIdx());
        userAuthority.setUserId(users.getUserId());
        userAuthority.setAuthority(users.getAuthority());
        userAuthorityRepository.save(userAuthority);
        System.out.println("권한등록");

        return userIdx;
    };

    /**
     * 사용자 삭제
     * @param userIdxArray
     * @return
     */
    @ResponseBody
    @PostMapping("/users/delete")
    public List<String> deleteSubmit(@RequestBody List<String> userIdxArray){
        log.info("boardIdxArray={}", userIdxArray);
        usersService.deleteUsers(userIdxArray);
        return userIdxArray;
    }


    @GetMapping("/users/update/{userIdx}")
    public String update(@PathVariable Long userIdx, Model model){

        Users getDetail = usersService.getDetail(userIdx);

        //게시판 상세 데이터
        model.addAttribute("users", getDetail);
        model.addAttribute("authority", userAuthorityRepository.findByUserId(getDetail.getUserId()).getAuthority());
        model.addAttribute("authIdx", userAuthorityRepository.findByUserId(getDetail.getUserId()).getAuthIdx());
        return "users/update";
    }

    //널 빈값 체크
    static boolean isStringEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
