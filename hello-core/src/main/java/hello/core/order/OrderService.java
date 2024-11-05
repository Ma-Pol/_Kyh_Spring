package hello.core.order;

public interface OrderService {
    /**
     * @param 회원 아이디
     * @param 상품 명
     * @param 상품 가격
     * @return 최종 주문 정보
     */
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
