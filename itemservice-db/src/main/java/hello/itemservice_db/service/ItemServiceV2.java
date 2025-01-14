package hello.itemservice_db.service;

import hello.itemservice_db.domain.Item;
import hello.itemservice_db.repository.ItemSearchCond;
import hello.itemservice_db.repository.ItemUpdateDto;
import hello.itemservice_db.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice_db.repository.v2.ItemRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceV2 implements ItemService {
    private final ItemRepositoryV2 itemRepository;
    private final ItemQueryRepositoryV2 itemQueryRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = itemRepository.findById(itemId).orElseThrow();

        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemQueryRepository.findAll(cond);
    }
}
