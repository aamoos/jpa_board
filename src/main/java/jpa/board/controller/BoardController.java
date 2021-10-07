package jpa.board.controller;

import jpa.board.entity.Board;
import jpa.board.entity.BoardFile;
import jpa.board.repository.BoardRepository;
import jpa.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @param session
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/board/main")
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

        //board 게시판 테이블 insert
        Long boardIdx = boardService.savePost(board);
        board.setBoardIdx(boardIdx);

        //board 파일 테이블 insert
        boardService.insertBoardFile(board);

        return boardIdx;
    };

    /**
     *  게시판 수정화면
     * @param boardIdx
     * @param model
     * @return
     */
    @GetMapping("/board/update/{boardIdx}")
    public String update(@PathVariable Long boardIdx, Model model){

        //게시판 상세 데이터
        model.addAttribute("board", boardService.getDetail(boardIdx));

        //게시판 파일 리스트
        model.addAttribute("boardFileInfo", boardService.selectBoardFile(boardIdx));
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
        Long boardIdx = boardService.savePost(board);

        //board 파일 테이블 insert
        boardService.insertBoardFile(board);

        System.out.println("deleteFileIdxs : " + board.getDeleteFileIdxs());

        //넘어온 파일 삭제 시퀀스 삭제처리
        if(!board.getDeleteFileIdxs().isEmpty()){
            String deleteFileIdxs = (String) board.getDeleteFileIdxs();
            String[] fileIdxsArray = deleteFileIdxs.split(",");
            System.out.println("fileIdxsArray : " + fileIdxsArray);
            //해당 시퀀스 삭제처리
            for(int i=0; i<fileIdxsArray.length; i++){
                String fileId = fileIdxsArray[i];
                System.out.println("fileId : " + fileId);
                boardService.deleteBoardFile(Long.parseLong(fileId));
            }
        }

        return boardIdx;
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
