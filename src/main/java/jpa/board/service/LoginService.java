package jpa.board.service;

import jpa.board.entity.UserAuthority;
import jpa.board.entity.Users;
import jpa.board.repository.UserAuthorityRepository;
import jpa.board.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsersRepository usersRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    //로그인체크
    public Map<String, Object> checkLoginInsert(Users users){
        Map<String, Object> result = new HashMap<String, Object>();
        log.info("users={}", users.toString());
        String userId = users.getUserId().toUpperCase();

        Users userInfo = usersRepository.findByUserIdAndUseYn(userId, "Y");
        log.info("userInfo={}", userInfo);

        //사용자가 없을경우에만 회원가입 처리
        if(userInfo == null){
            //넘어온 패스워드 encode
            users.setPassword(passwordEncoder.encode(users.getPassword()));

            //아이디 저장
            usersRepository.save(users);

            //해당 아이디 권한 저장
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setUserId(users.getUserId());
            userAuthority.setAuthority("USER");
            userAuthorityRepository.save(userAuthority);
            result.put("resultCode" , "OK");
            result.put("resultMsg", "회원가입이 정상적으로 되었습니다.");
        }

        else{
            result.put("resultCode" , "FAIL");
            result.put("resultMsg", "중복된 회원이 있습니다.");
        }

        return result;
    }
}