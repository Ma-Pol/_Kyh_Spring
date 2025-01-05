package hello.itemservice_db.repository.v2;

import hello.itemservice_db.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
