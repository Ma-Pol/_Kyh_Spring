package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    // Member: 다, Team: 일
    // => 멤버 기준으로 다대일(ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 연관관계 편의 메서드
    public void setTeam(Team team) {
        team.getMembers().add(this);
        this.team = team;
    }
}