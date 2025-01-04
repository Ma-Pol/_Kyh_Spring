package hello.itemservice_db.config;

import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.jpa.JpaItemRepositoryV2;
import hello.itemservice_db.repository.jpa.SpringDataJpaItemRepository;
import hello.itemservice_db.service.ItemService;
import hello.itemservice_db.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {
    private final SpringDataJpaItemRepository jpaItemRepository;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV2(jpaItemRepository);
    }
}
