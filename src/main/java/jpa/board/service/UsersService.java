package jpa.board.service;

import jpa.board.entity.Board;
import jpa.board.entity.BoardFile;
import jpa.board.entity.Users;
import jpa.board.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;

    /**
     * 사용자 관리 페이징
     * @param page
     * @param keyword
     * @return
     */

    public Page<Users> list(int page, String keyword){

        //keyword가 없을경우
        if(keyword.isEmpty()){
            return usersRepository.findAllByUseYn(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "userIdx")), "Y");
        }

        //keyword가 있을경우
        else{
            return usersRepository.findAllByUserIdContainingIgnoreCaseAndUseYn(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "userIdx")), keyword, "Y");
        }
    }

    /**
     * 사용자 저장
     * @param users
     * @return
     */
    public Long savePost(Users users){
        return usersRepository.save(users).getUserIdx();
    }

    /**
     * 사용자 삭제
     * @param userIdxArray
     */
    public void deleteUsers(List<String> userIdxArray){
        for(int i=0; i<userIdxArray.size(); i++) {
            String userIdx = userIdxArray.get(i);
            Optional<Users> optional = usersRepository.findById(Long.parseLong(userIdx));
            if(optional.isPresent()){
                Users users = optional.get();
                users.setUseYn("N");
                usersRepository.save(users);
            }
            else{
                throw new NullPointerException();
            }
        }
    }

    public Users getDetail(Long userIdx){
        Optional<Users> optional = usersRepository.findById(userIdx);
        if(optional.isPresent()){
            Users users = optional.get();
            usersRepository.save(users);
            return users;
        }
        else{
            Users users = new Users();
            return users;
        }
    }
}
