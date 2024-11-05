package hello.core.discount;

import hello.core.member.Member;

import static hello.core.member.Grade.VIP;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000; // 1000원 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGarde() == VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
