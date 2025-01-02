package hello.itemservice_db;

import hello.itemservice_db.config.JdbcTemplateConfig;
import hello.itemservice_db.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

//@Import(MemoryConfig.class)
@Import(JdbcTemplateConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice_db.web")
public class ItemserviceDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemserviceDbApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(ItemRepository itemRepository) {
        return new TestDataInit(itemRepository);
    }

}
