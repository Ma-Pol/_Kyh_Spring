package hello.itemservice_db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity // JPA 가 사용하는 객체
public class Item {
    @Id // 테이블의 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 값 생성 타입
    private Long id;

    @Column(name = "item_name", length = 10) // 스네이크-카멜 변환은 자동으로 해주니 생략 가능
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
