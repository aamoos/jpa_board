package jpa.board.repository;

import jpa.board.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

    //아이디로 검색
    Users findByUserIdAndUseYn(String userId, String useYn);
}
