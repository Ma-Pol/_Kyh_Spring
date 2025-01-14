package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Integer price;

    private Integer stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    public void addStock(@NotNull Integer quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(@NotNull Integer quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            log.info("상품 수량 오류 - 재고 부족");
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    }
}
