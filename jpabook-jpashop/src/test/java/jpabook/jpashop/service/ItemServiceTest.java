package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 등록")
    void addNewItem() {
        // given
        Item album = new Album();
        album.setName("album");
        album.setPrice(10_000);
        album.setStockQuantity(100);

        // when
        itemService.saveItem(album);

        // then
        Item findItem = itemRepository.findById(album.getId());
        assertThat(findItem.getId()).isEqualTo(album.getId());
        assertThat(findItem).isEqualTo(album);
    }

    @Test
    @DisplayName("상품 정보 변경")
    void updateItem() {
        // given
        Item album = new Album();
        album.setName("album");
        album.setPrice(10_000);
        album.setStockQuantity(100);
        itemService.saveItem(album);

        // when
        album.setName("album2");
        album.setPrice(5_000);
        album.setStockQuantity(50);
        itemService.saveItem(album);

        // then
        Item findAlbum = itemRepository.findById(album.getId());
        assertThat(findAlbum.getId()).isEqualTo(album.getId());
        assertThat(findAlbum.getName()).isEqualTo("album2");
        assertThat(findAlbum.getPrice()).isEqualTo(5_000);
        assertThat(findAlbum.getStockQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("상품 구매 - 수량 감소")
    void decreaseQuantity() {
        // given
        Item album = new Album();
        album.setName("album");
        album.setPrice(10_000);
        album.setStockQuantity(100);
        itemService.saveItem(album);

        // when
        album.removeStock(10);
        itemService.saveItem(album);

        // then
        Item findAlbum = itemRepository.findById(album.getId());
        assertThat(findAlbum.getStockQuantity()).isEqualTo(90);
    }

    @Test
    @DisplayName("상품 구매 취소 - 수량 증가")
    void increaseQuantity() {
        // given
        Item album = new Album();
        album.setName("album");
        album.setPrice(10_000);
        album.setStockQuantity(100);
        itemService.saveItem(album);

        // when
        album.addStock(10);
        itemService.saveItem(album);

        // then
        Item findAlbum = itemRepository.findById(album.getId());
        assertThat(findAlbum.getStockQuantity()).isEqualTo(110);
    }

    @Test
    @DisplayName("상품 구매 예외 - 수량 초과 구매")
    void decreaseQuantityEx() {
        // given
        Item album = new Album();
        album.setName("album");
        album.setPrice(10_000);
        album.setStockQuantity(100);
        itemService.saveItem(album);

        // when, then
        assertThatThrownBy(() -> album.removeStock(110))
                .isInstanceOf(NotEnoughStockException.class);
    }
}