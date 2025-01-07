package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService service;

    @Autowired
    OrderRepository repository;

    @Test
    void complete() throws NotEnoughMoneyException {
        // given
        Order order = new Order();
        order.setUsername("정상");

        // when
        service.order(order);

        // then
        Order findOrder = repository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() {
        // given
        Order order = new Order();
        order.setUsername("예외");

        // when, then
        assertThatThrownBy(() -> service.order(order))
                .isInstanceOf(RuntimeException.class);

        // then
        Order findOrder = repository.findById(order.getId()).orElse(null);
        assertThat(findOrder).isNull();
    }

    @Test
    void bizException() {
        // given
        Order order = new Order();
        order.setUsername("잔고 부족");

        // when
        try {
            service.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }

        // then
        Order findOrder = repository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }
}