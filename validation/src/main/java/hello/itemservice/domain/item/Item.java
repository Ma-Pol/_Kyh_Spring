package hello.itemservice.domain.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "가격과 수량의 합은 10,000 이상이어야 합니다")
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

/*
    @NotNull(groups = UpdateCheck.class) // 수정 시에만 적용
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1_000, max = 1_000_000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9_999, groups = SaveCheck.class)
    private Integer quantity;
*/

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
