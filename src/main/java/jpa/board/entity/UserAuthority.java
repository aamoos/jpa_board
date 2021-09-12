package jpa.board.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authIdx;

    //사용자 id
    private String userId;

    //사용자 권한
    private String authority;

    @Override
    public String toString() {
        return "UserAuthority{" +
                "authIdx=" + authIdx +
                ", userId='" + userId + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        // TODO Auto-generated method stub
        return this.authority;
    }
}
