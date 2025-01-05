package hello.itemservice_db.repository.v2;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice_db.domain.Item;
import hello.itemservice_db.repository.ItemSearchCond;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static hello.itemservice_db.domain.QItem.item;

@Repository
public class ItemQueryRepositoryV2 {
    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return query
                .select(item)
                .from(item)
                .where(containsItemName(itemName), loePrice(maxPrice))
                .fetch();
    }

    private BooleanExpression containsItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.contains(itemName);
        }

        return null;
    }

    private BooleanExpression loePrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }

        return null;
    }
}
