package hello.upload.repository;

import hello.upload.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> store = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong(0);

    @Override
    public Item save(Item item) {
        item.setId(sequence.addAndGet(1));
        store.put(item.getId(), item);

        return item;
    }

    @Override
    public Item findById(Long id) {
        return store.get(id);
    }
}
