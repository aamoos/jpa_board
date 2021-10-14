package jpa.board.controller;

import jpa.board.entity.Api;
import jpa.board.entity.Board;
import jpa.board.service.ApiService;
import jpa.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * api 관리 controller
 */

@Controller
@Slf4j
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/api/list")
    public String list(Model model
            , @RequestParam(required = false, defaultValue = "0", value = "page") int page
            , @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword){

        putListModel(model, page, keyword);
        return "api/list";
    }

    /**
     * api 등록화면
     * @return
     */
    @GetMapping("/api/write")
    public String write(){
        return "api/write";
    }

    /**
     * api 등록
     * @param api
     * @return
     */
    @ResponseBody
    @PostMapping("/api/write")
    public Long writeSubmit(@RequestBody Api api){
        log.info("params={}", api.toString());
        return apiService.insertApi(api);
    };

    /**
     * api 수정화면
     * @param apiIdx
     * @param model
     * @return
     */
    @GetMapping("/api/update/{apiIdx}")
    public String update(@PathVariable Long apiIdx, Model model){
        putDetailModel(apiIdx, model);
        return "api/update";
    }

    /**
     * api 업데이트
     * @param api
     * @return
     */
    @ResponseBody
    @PostMapping("/api/update")
    public Long updateSubmit(@RequestBody Api api){
        log.info("params={}", api.toString());
        return apiService.savePost(api);
    }

    /**
     * api 삭제
     * @param apiIdxArray
     * @return
     */
    @ResponseBody
    @PostMapping("/api/delete")
    public List<String> deleteSubmit(@RequestBody List<String> apiIdxArray){
        apiService.deleteApi(apiIdxArray);
        return apiIdxArray;
    }

    @ResponseBody
    @PostMapping("/api/getAddressData")
    public Map<String, Object> getAddressData(@RequestBody Api api){
        return apiService.checkApiDomain(api);
    }

    private void putListModel(Model model, int page, String keyword) {
        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Api> listPage =  apiService.list(page, keyword);

        //총페이지수
        model.addAttribute("api", listPage.getContent());
        model.addAttribute("totalPage", listPage.getTotalPages());

        //페이지번호
        model.addAttribute("pageNo", page);

        //리스트 총갯수
        model.addAttribute("resultDataTotal", listPage.getTotalElements());

        //페이징 사이즈
        model.addAttribute("size", listPage.getSize());

        //페이지 넘버
        model.addAttribute("number", listPage.getNumber());

        //검색키워드
        model.addAttribute("keyword", keyword);
    }

    private void putDetailModel(Long apiIdx, Model model) {
        //게시판 상세 데이터
        model.addAttribute("api", apiService.getDetail(apiIdx));

    }

}
