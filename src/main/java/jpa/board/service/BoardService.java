package jpa.board.service;

import jpa.board.entity.Board;
import jpa.board.entity.BoardFile;
import jpa.board.repository.BoardFileRepository;
import jpa.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final EntityManager em;

    /**
     * 게시판 목록 페이징
     * @param page
     * @return
     */
    public Page<Board> list(int page, String keyword){

        //keyword가 없을경우
        if(keyword.isEmpty()){
            return boardRepository.findAllByUseYn(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardIdx")), "Y");
        }

        //keyword가 있을경우
        else{
            return boardRepository.findAllByBoardTitleContainingIgnoreCaseAndUseYn(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardIdx")), keyword, "Y");
        }
    }

    /**
     * 게시판 상세
     * @param boardIdx
     * @return
     */
    public Board getDetail(Long boardIdx){
        Optional<Board> optional = boardRepository.findById(boardIdx);
        if(optional.isPresent()){
            Board board = optional.get();
            board.setViewCount(board.getViewCount()+1);
            boardRepository.save(board);
            return board;
        }
        else{
            Board board = new Board();
            return board;
        }
    }

    /**
     * 게시판 저장
     * @param board
     * @return
     */
    public Long savePost(Board board){
        return boardRepository.save(board).getBoardIdx();
    }

    /**
        게시판 use_yn N으로 업데이트
     */
    public void deleteBoard(List<String> boardIdxArray){
        for(int i=0; i<boardIdxArray.size(); i++) {
            String boardIdx = boardIdxArray.get(i);
            Optional<Board> optional = boardRepository.findById(Long.parseLong(boardIdx));
            if(optional.isPresent()){
                Board board = optional.get();
                board.setUseYn("N");
                boardRepository.save(board);
            }
            else{
                throw new NullPointerException();
            }
        }
    }

    //게시판 파일 등록
    @Transactional
    public void insertBoardFile(Board board) {
        //파일 등록할게 있을경우만
        if(board.getFileIdxs() != null) {
            //파일 등록
            String fileIdxs = ((String) board.getFileIdxs()).replace("[", "").replace("]", "");
            String[] fileIdxArray = fileIdxs.split(",");

            for (int i=0; i<fileIdxArray.length; i++) {
                String fileIdx = fileIdxArray[i].trim();
                BoardFile boardFile = new BoardFile(board.getBoardIdx(), Long.parseLong(fileIdx),"Y") ;
                boardFileRepository.save(boardFile);
            }
        }
    }

    @Transactional
    public void deleteBoardFile(Long fileId){
        boardFileRepository.deleteById(fileId);
    }

    //boardIdx로 해당 게시물 파일리스트 조회
    public List<BoardFile> selectBoardFile(Long boardIdx){
        List<BoardFile> fileInfo = boardFileRepository.findByBoardIdx(boardIdx);

        return fileInfo;
    }

}
