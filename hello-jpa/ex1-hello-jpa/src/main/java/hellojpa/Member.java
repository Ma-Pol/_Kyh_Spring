package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    // 다대일 단방향
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "team_id")
//    private Team team;

    // 일대다 양방향
    // insert, update 기능을 무효화시켜 연관관계 주인의 능력을 강제로 상실시킴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;

    // ===============

    // 일대일(member 테이블에 외래키 존재) 단방향
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "locker_id")
//    @Column(unique = true) // 일대일 관계에서는 외래키에 유니크 조건을 거는 것이 좋음
//    private Locker locker;

    // 일대일(locker 테이블에 외래키 존재) 양방향
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private Locker locker;

    // ===============

    // 다대다(추천하지 않음)
//    @ManyToMany
//    @JoinTable(name = "member_product")
//    private List<Product> products = new ArrayList<>();

    // 다대다(다대일 - 일대다 풀이)
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}