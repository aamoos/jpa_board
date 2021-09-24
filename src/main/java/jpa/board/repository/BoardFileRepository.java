package jpa.board.repository;

import jpa.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findByBoardIdx(Long boardIdx);
}
