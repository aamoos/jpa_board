package jpa.board.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @Column(columnDefinition = "varchar(45) not null comment '타이틀'")
    private String boardTitle;

    @Column(columnDefinition = "TEXT not null comment '내용'")
    private String boardContent;

    @Column(columnDefinition = "varchar(45) not null comment '등록자'")
    private String regId;

    //조회수
    private int viewCount;

    private String useYn;

    //insert시에 현재시간을 읽어서 저장
    @CreationTimestamp
    private LocalDateTime regDate;

    //update시에 현재시간을 읽여서 저장
    @UpdateTimestamp
    private LocalDateTime uptDate;

}
