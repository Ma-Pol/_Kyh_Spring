package hello.upload.repository;

import hello.upload.domain.Item;

public interface ItemRepository {
    Item save(Item item);

    Item findById(Long id);
}
