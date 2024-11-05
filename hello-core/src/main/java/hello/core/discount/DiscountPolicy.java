package hello.core.discount;

import hello.core.member.Member;

public interface DiscountPolicy {
    /**
     * @param 회원 정보
     * @param 상품 가격
     * @return 할인 금액
     */
    int discount(Member member, int price);
}
