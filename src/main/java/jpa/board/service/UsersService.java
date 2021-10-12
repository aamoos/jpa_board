package jpa.board.service;

import jpa.board.entity.UserAuthority;
import jpa.board.entity.Users;
import jpa.board.repository.UserAuthorityRepository;
import jpa.board.repository.UsersRepository;
import jpa.board.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthorityRepository userAuthorityRepository;

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

    /**
     * 사용자 상세보기
     * @param userIdx
     * @return
     */
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

    /**
     * 사용자 등록
     * @param users
     * @return
     */
    public Map<String, Object> insertUser(Users users){

        Map<String, Object> result = new HashMap<String, Object>();

        //회원 중복체크
        Users checkUsers = usersRepository.findByUserIdAndUseYn(users.getUserId(), "Y");

        if(checkUsers == null){
            String fileIdx = users.getFileIdxs();
            if(!StringUtils.isStringEmpty(users.getFileIdxs())){
                fileIdx = ((String) users.getFileIdxs()).replace("[", "").replace("]", "");
                users.setFileIdx(Long.parseLong(fileIdx));
            }

            //패스워드 암호화
            users.setPassword(passwordEncoder.encode(users.getPassword()));

            //board 게시판 테이블 insert
            Long userIdx = savePost(users);

            log.info("users={}", users.toString());

            //권한설정
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setAuthIdx(users.getAuthIdx());
            userAuthority.setUserId(users.getUserId());
            userAuthority.setAuthority(users.getAuthority());
            userAuthorityRepository.save(userAuthority);
            result.put("result", "OK");
            result.put("resultMsg", "해당사용자가 정상적으로 등록되었습니다.");
        }

        else{
            result.put("result", "DUP");
            result.put("resultMsg", "해당사용자와 중복된 회원이 있습니다.");
        }

        return result;
    }

    /**
     * 사용자 수정
     * @param users
     * @return
     */
    public Map<String, Object> updateUser(Users users){
        Map<String, Object> result = new HashMap<String, Object>();
        String fileIdx = users.getFileIdxs();
        if(!StringUtils.isStringEmpty(users.getFileIdxs())){
            fileIdx = ((String) users.getFileIdxs()).replace("[", "").replace("]", "");
            users.setFileIdx(Long.parseLong(fileIdx));
        }

        //패스워드 암호화
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        //board 게시판 테이블 insert
        Long userIdx = savePost(users);

        log.info("users={}", users.toString());

        //권한설정
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthIdx(users.getAuthIdx());
        userAuthority.setUserId(users.getUserId());
        userAuthority.setAuthority(users.getAuthority());
        userAuthorityRepository.save(userAuthority);
        result.put("result", "OK");
        result.put("resultMsg", "해당사용자가 정상적으로 등록되었습니다.");
        return result;
    }

}
