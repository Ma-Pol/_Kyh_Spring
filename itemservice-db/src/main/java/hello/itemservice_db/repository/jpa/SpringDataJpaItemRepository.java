package hello.itemservice_db.repository.jpa;

import hello.itemservice_db.domain.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 기본적인 CRUD 기능은 JpaRepository 가 제공 <br />
 * 이름 조회, 가격 조회, 이름 + 가격 조회 기능은 직접 작성
 */
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemNameLike(String itemName);

    List<Item> findByItemNameContaining(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    // 쿼리 메서드(아래 메서드와 같은 기능을 수행한다.)
    List<Item> findByItemNameContainingAndPriceLessThanEqual(String itemName, Integer price);

    // 쿼리 직접 실행
    @Query("SELECT i FROM Item i WHERE i.itemName LIKE ('%' || :itemName || '%') AND i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
