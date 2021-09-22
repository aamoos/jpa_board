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
import java.util.Map;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @GetMapping("/")
    public String list(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Board> listPage =  boardService.list(page);

        //총페이지수
        int totalPage = listPage.getTotalPages();

        model.addAttribute("board", listPage.getContent());
        model.addAttribute("totalPage", totalPage);

        log.info("totalPage={}", totalPage);

        return "main/list";
    }

    @GetMapping("/board/write")
    public String write(){
        return "main/write";
    }

    //게시판 저장
    @ResponseBody
    @PostMapping("/board/write")
    public Board writeSubmit(@RequestBody Board board){
        log.info("params={}", board);

        boardRepository.save(board);
        return board;
    };

    @GetMapping("/board/update")
    public String update(){
        return "main/update";
    }

}
