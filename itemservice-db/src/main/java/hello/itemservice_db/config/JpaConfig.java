package hello.itemservice_db.config;

import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.jpa.JpaItemRepositoryV1;
import hello.itemservice_db.service.ItemService;
import hello.itemservice_db.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {
    private final EntityManager em;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV1(em);
    }
}
