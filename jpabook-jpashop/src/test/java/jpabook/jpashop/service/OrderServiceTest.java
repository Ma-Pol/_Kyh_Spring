package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired // 원칙적으로는 PersistenceContext 를 사용
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문")
    void order() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        Integer orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findById(orderId);

        assertThat(findOrder.getStatus()).isEqualByComparingTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems()).hasSize(1);
        assertThat(findOrder.getTotalPrice()).isEqualTo(10000 * 2);
        assertThat(item.getStockQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("상품 주문 - 재고 수량 초과 주문")
    void orderEx() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        Integer orderCount = 11; // 재고보다 많은 수량

        // when, then
        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    @DisplayName("주문 취소")
    void cancelOrder() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        Integer orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findById(orderId);

        assertThat(findOrder.getStatus()).isEqualByComparingTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity()).isEqualTo(10);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));

        em.persist(member);

        return member;
    }

    private Book createBook(String name, Integer price, Integer stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);

        em.persist(book);

        return book;
    }
}