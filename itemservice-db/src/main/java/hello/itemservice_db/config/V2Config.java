package hello.itemservice_db.config;

import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice_db.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice_db.repository.v2.ItemRepositoryV2;
import hello.itemservice_db.service.ItemService;
import hello.itemservice_db.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class V2Config {
    private final EntityManager em;
    private final ItemRepositoryV2 itemRepository; // Spring Data JPA

    @Bean
    public ItemService itemService() {
        // SpringDataJPA Repository, QueryDSL Repository
        return new ItemServiceV2(itemRepository, itemQueryRepository());
    }

    @Bean
    public ItemQueryRepositoryV2 itemQueryRepository() {
        return new ItemQueryRepositoryV2(em);
    }

    // TestDataInit 용
    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }
}
