package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    // 양방향 연관관계 매핑
    @OneToMany(mappedBy = "team") // Member 의 변수명 team 과 매핑되었음을 명시, 연관관계의 주인이 아님을 명시
    List<Member> members = new ArrayList<>();
}
