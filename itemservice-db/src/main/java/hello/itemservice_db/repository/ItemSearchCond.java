package hello.itemservice_db.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchCond {
    private String itemName;
    private Integer maxPrice;
}
