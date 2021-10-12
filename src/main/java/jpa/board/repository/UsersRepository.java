package jpa.board.repository;

import jpa.board.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

    //아이디로 검색
    Users findByUserIdAndUseYn(String userId, String useYn);

    //useYn 조건 find
    Page<Users> findAllByUseYn(Pageable pageable, String useYn);

    //useYn boardTitle 조건
    Page<Users> findAllByUserIdContainingIgnoreCaseAndUseYn(Pageable pageable, String boardTitle, String useYn);
}
