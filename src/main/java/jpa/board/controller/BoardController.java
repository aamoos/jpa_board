package jpa.board.controller;

import jpa.board.entity.Board;
import jpa.board.repository.BoardRepository;
import jpa.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    /**
     * 게시판 목록화면
     * @param session
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/")
    public String list(HttpSession session, Model model
            , @RequestParam(required = false, defaultValue = "0", value = "page") int page
            , @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword){

        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Board> listPage =  boardService.list(page, keyword);

        //총페이지수
        int totalPage = listPage.getTotalPages();
        model.addAttribute("board", listPage.getContent());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageNo", page);
        model.addAttribute("resultDataTotal", listPage.getTotalElements());
        model.addAttribute("size", listPage.getSize());
        model.addAttribute("number", listPage.getNumber());
        model.addAttribute("keyword", keyword);

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
        return boardService.savePost(board);
    };

    /**
     *  게시판 수정화면
     * @param boardIdx
     * @param model
     * @return
     */
    @GetMapping("/board/update/{boardIdx}")
    public String update(@PathVariable Long boardIdx, Model model){
        log.info("boardIdx={}", boardIdx);
        Board boardDetail = boardService.getDetail(boardIdx);
        log.info("boardDetail={}", boardDetail);
        model.addAttribute("board", boardDetail);
        return "main/update";
    }

    /**
     * 게시판 수정화면
     * @param board
     * @return
     */
    @ResponseBody
    @PostMapping("/board/update")
    public Long updateSubmit(@RequestBody Board board){
        log.info("params={}", board);
        return boardService.savePost(board);
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
}
