package jpa.board.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    //사용자 id
    @Column(columnDefinition = "varchar(45) not null comment '사용자 아이디'")
    private String userId;

    //패스워드
    @Column(columnDefinition = "varchar(100) not null comment '패스워드'")
    private String password;

    //사용자 이름
    @Column(columnDefinition = "varchar(20) comment '사용자 이름'")
    private String userName;

    //이메일
    @Column(columnDefinition = "varchar(45) comment '이메일'")
    private String email;

    //핸드폰이름
    @Column(columnDefinition = "varchar(45) comment '이메일'")
    private String handPhoneNo;

    //사용여부
    @Column(columnDefinition = "varchar(1) default 'Y' comment '사용여부'")
    private String useYn;

    @Transient
    public List<UserAuthority> authorities;

    @Override
    public String toString() {
        return "Users{" +
                "userIdx=" + userIdx +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", handPhoneNo='" + handPhoneNo + '\'' +
                ", useYn='" + useYn + '\'' +
                ", authorities=" + authorities +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
