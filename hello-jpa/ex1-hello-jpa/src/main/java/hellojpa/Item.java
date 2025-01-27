package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
//@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 단일 테이블 전략
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 개별 테이블 전략(추천하지 않음)
@DiscriminatorColumn(name = "item_type") // item 테이블에 각 객체의 이름이 자동 입력
@Getter
@Setter
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 개별 테이블 전략에선 TABLE 사용
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Integer price;
}
