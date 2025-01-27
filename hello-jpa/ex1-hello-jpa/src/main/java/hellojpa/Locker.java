package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locker_id")
    private Long id;

    private String name;

    // 일대일(member 테이블에 외래키 존재) 양방향
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "locker")
//    private Member member;

    // 일대일(locker 테이블에 외래키 존재) 단방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
