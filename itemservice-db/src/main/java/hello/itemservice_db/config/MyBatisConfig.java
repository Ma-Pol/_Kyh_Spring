package hello.itemservice_db.config;

import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.mybatis.ItemMapper;
import hello.itemservice_db.repository.mybatis.MyBatisItemRepository;
import hello.itemservice_db.service.ItemService;
import hello.itemservice_db.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
//@MapperScan(basePackages = "hello.itemservice_db.repository.mybatis") // 지정하지 않아도 문제 없음
public class MyBatisConfig {
    private final ItemMapper itemMapper;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MyBatisItemRepository(itemMapper);
    }
}
