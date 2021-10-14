package jpa.board.controller;

import jpa.board.entity.Board;
import jpa.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public void main(HttpServletResponse response) throws IOException {
        response.sendRedirect("/board/main");
    }

    /**
     * 게시판 목록화면
     * @param model
     * @param page
     * @param keyword
     * @return
     */
    @GetMapping("/board/main")
    public String list(Model model
            , @RequestParam(required = false, defaultValue = "0", value = "page") int page
            , @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword){

        putListModel(model, page, keyword);

        return "main/list";
    }

    /**
     * 게시판 등록화면
     * @return
     */
    @GetMapping("/board/write")
    public String write(){
        return "main/write";
    }

    /**
     * 게시판 등록
     * @param board
     * @return
     */
    @ResponseBody
    @PostMapping("/board/write")
    public Long writeSubmit(@RequestBody Board board){
        log.info("params={}", board);
        return boardService.insertBoard(board);
    };

    /**
     *  게시판 수정화면
     * @param boardIdx
     * @param model
     * @return
     */
    @GetMapping("/board/update/{boardIdx}")
    public String update(@PathVariable Long boardIdx, Model model){
        putDetailModel(boardIdx, model);
        return "main/update";
    }

    /**
     * 게시판 수정
     * @param board
     * @return
     */
    @ResponseBody
    @PostMapping("/board/update")
    public Long updateSubmit(@RequestBody Board board){
        log.info("params={}", board);
        return boardService.updateBoard(board);
    }

    /**
     * 게시판 삭제기능
     * @param boardIdxArray
     * @return
     */

    @ResponseBody
    @PostMapping("/board/delete")
    public List<String> deleteSubmit(@RequestBody List<String> boardIdxArray){
        log.info("boardIdxArray={}", boardIdxArray);
        boardService.deleteBoard(boardIdxArray);
        return boardIdxArray;
    }

    /**
     * 리스트 모델 넣기
     * @param model
     * @param page
     * @param keyword
     */
    private void putListModel(Model model, int page, String keyword) {
        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Board> listPage =  boardService.list(page, keyword);

        //총페이지수
        model.addAttribute("board", listPage.getContent());
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


    /**
     * 상세 모델 넣기
     * @param boardIdx
     * @param model
     */
    private void putDetailModel(Long boardIdx, Model model) {
        //게시판 상세 데이터
        model.addAttribute("board", boardService.getDetail(boardIdx));

        //게시판 파일 리스트
        model.addAttribute("boardFileInfo", boardService.selectBoardFile(boardIdx));
    }
}
