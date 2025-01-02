package hello.itemservice_db.config;

import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.jdbctemplate.JdbcTemplateItemRepositoryV3;
import hello.itemservice_db.service.ItemService;
import hello.itemservice_db.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfig {
    private final DataSource dataSource;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
//        return new JdbcTemplateItemRepositoryV1(dataSource);
//        return new JdbcTemplateItemRepositoryV2(dataSource);
        return new JdbcTemplateItemRepositoryV3(dataSource);
    }
}
