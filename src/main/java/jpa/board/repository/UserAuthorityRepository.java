package jpa.board.repository;

import jpa.board.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

    //아이디로 검색
    UserAuthority findByUserId(String userId);

}
