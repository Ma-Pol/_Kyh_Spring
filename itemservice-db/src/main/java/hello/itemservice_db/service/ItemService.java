package hello.itemservice_db.service;

import hello.itemservice_db.domain.Item;
import hello.itemservice_db.repository.ItemSearchCond;
import hello.itemservice_db.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
