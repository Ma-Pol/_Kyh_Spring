package hello.itemservice_db.config;

import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.memory.MemoryItemRepository;
import hello.itemservice_db.service.ItemService;
import hello.itemservice_db.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
