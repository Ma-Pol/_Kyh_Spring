package hello.itemservice_db;

import hello.itemservice_db.config.JdbcTemplateConfig;
import hello.itemservice_db.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
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
/*
    // 테스트용 메모리 데이터베이스 설정
    // 아무 설정도 되어 있지 않을 경우, 스프링 부트는 직접 임베디드 DB를 생성한다.(가능한 경우)
    @Bean
    @Profile("test")
    public DataSource dataSource() {
        log.info("메모리 데이터베이스 초기화");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }
*/
}
