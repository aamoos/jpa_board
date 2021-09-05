package jpa.board.repository;

import jpa.board.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    //아이디로 검색
    Users findByUserId(String userId);
}
