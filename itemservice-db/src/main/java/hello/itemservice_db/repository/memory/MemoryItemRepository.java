package hello.itemservice_db.repository.memory;

import hello.itemservice_db.domain.Item;
import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.ItemSearchCond;
import hello.itemservice_db.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryItemRepository implements ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>(); // static
    private static long sequence = 0L; // static

    @Override
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();

        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return store.values().stream()
                .filter(item -> {
                    // 검색 조건(itemName)이 없다면 필터링 X
                    if (ObjectUtils.isEmpty(itemName)) {
                        return true;
                    }

                    // 검색 조건(itemName)이 있다면 필터링 O
                    return item.getItemName().contains(itemName);
                })
                .filter(item -> {
                    // 검색 조건(maxPrice)이 없다면 필터링 X
                    if (maxPrice == null) {
                        return true;
                    }

                    // 검색 조건(maxPrice)이 있다면 필터링 O
                    return item.getPrice() <= maxPrice;
                })
                .collect(Collectors.toList());
    }

    public void clearStore() {
        store.clear();
    }

}
