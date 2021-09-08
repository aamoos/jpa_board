package jpa.board.repository;

import jpa.board.entity.UserAuthority;
import jpa.board.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersRepositoryTest {

    @Autowired UsersRepository usersRepository;
    @Autowired UserAuthorityRepository userAuthorityRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void join(){

        Users users = new Users();
        users.setUserId("1");
        users.setPassword("1");

        usersRepository.save(users);

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserId(users.getUserId());
        userAuthority.setAuthority("USER");

        userAuthorityRepository.save(userAuthority);

        //userAuthorityRepository.save(users);
    }

}