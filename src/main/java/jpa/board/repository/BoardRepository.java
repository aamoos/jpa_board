package jpa.board.repository;

import jpa.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

    //useYn 조건 find
    Page<Board> findAllByUseYn(Pageable pageable, String useYn);

    //useYn boardTitle 조건
    Page<Board> findAllByBoardTitleContainingIgnoreCaseAndUseYn(Pageable pageable, String boardTitle, String useYn);

}
