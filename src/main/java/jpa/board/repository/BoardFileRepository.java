package jpa.board.repository;

import jpa.board.entity.Board;
import jpa.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends CrudRepository<BoardFile, Long> {

    @Query(value =
            "SELECT \n"+
                    "T1.FILE_ID\n"+
                    ",T1.BOARD_IDX\n"+
                    ",T1.USE_YN\n"+
                    ",T2.ORIG_NM AS ORIG_NM\n"+
            "FROM \n"+
            "board_file T1\n"+
            ",file T2\n"+
            "WHERE\n"+
            "T1.FILE_ID = T2.FILE_IDX\n"+
            "AND T1.USE_YN = 'Y'\n"+
            "AND T1.BOARD_IDX = :boardIdx\n"
            , nativeQuery = true
    )
    List<BoardFile> findByBoardIdx(Long boardIdx);

}
