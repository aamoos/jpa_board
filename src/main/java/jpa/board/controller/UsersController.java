package jpa.board.controller;

import jpa.board.entity.Users;
import jpa.board.repository.UserAuthorityRepository;
import jpa.board.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 *  사용자 관리 controller
 */

@Controller
@Slf4j
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final UserAuthorityRepository userAuthorityRepository;

    /**
     * 사용자 리스트 화면
     * @param page
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/users/list")
    public String list(@RequestParam(required = false, defaultValue = "0", value = "page") int page
                     , @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword
                     , Model model){

        putListModel(page, keyword, model);

        return "users/list";
    }

    /**
     * 사용자 등록화면
     * @return
     */
    @GetMapping("/users/write")
    public String write(){
        return "users/write";
    }

    /**
     * 사용자 수정 상세 보기
     * @param userIdx
     * @param model
     * @return
     */
    @GetMapping("/users/update/{userIdx}")
    public String update(@PathVariable Long userIdx, Model model){

        putDetailModel(userIdx, model);
        return "users/update";
    }

    /**
     * 사용자 등록
     * @param users
     * @return
     */
    @ResponseBody
    @PostMapping("/users/write")
    public Map<String, Object> writeSubmit(@RequestBody Users users){
        log.info("fileIdxs={}", users.getFileIdxs());
        return usersService.insertUser(users);
    };

    /**
     * 사용자 수정
     * @param users
     * @return
     */
    @ResponseBody
    @PostMapping("/users/update")
    public Map<String, Object> updateSubmit(@RequestBody Users users){
        log.info("fileIdxs={}", users.getFileIdxs());
        return usersService.updateUser(users);
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

    /**
     * 리스트 모델 넣기
     * @param page
     * @param keyword
     * @param model
     */
    private void putListModel(int page, String keyword, Model model) {
        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Users> listPage =  usersService.list(page, keyword);

        //총페이지수
        model.addAttribute("users", listPage.getContent());
        model.addAttribute("totalPage", listPage.getTotalPages());
        model.addAttribute("pageNo", page);
        model.addAttribute("resultDataTotal", listPage.getTotalElements());
        model.addAttribute("size", listPage.getSize());
        model.addAttribute("number", listPage.getNumber());
        model.addAttribute("keyword", keyword);
    }

    /**
     * 상세 모델넣기
     * @param userIdx
     * @param model
     */
    private void putDetailModel(Long userIdx, Model model) {
        Users getDetail = usersService.getDetail(userIdx);

        //게시판 상세 데이터
        model.addAttribute("users", getDetail);
        model.addAttribute("authority", userAuthorityRepository.findByUserId(getDetail.getUserId()).getAuthority());
        model.addAttribute("authIdx", userAuthorityRepository.findByUserId(getDetail.getUserId()).getAuthIdx());
    }
}
