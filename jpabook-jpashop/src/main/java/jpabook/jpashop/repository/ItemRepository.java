package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        em.persist(item);

        // 병합(merge) 대신 변경감지를 사용
//        if (item.getId() == null) {
//            em.persist(item); // 상품 id가 없으면 신규 상품이므로 추가
//        } else {
//            em.merge(item); // 상품 id가 있으면 기존 상품이므로 변경
//        }
    }

    public Item findById(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("SELECT i FROM Item i", Item.class)
                .getResultList();
    }
}
