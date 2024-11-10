package hello.core.discount;

import hello.core.annotatioin.MainDiscountPolicy;
import hello.core.member.Member;
import org.springframework.stereotype.Component;

import static hello.core.member.Grade.VIP;

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {
    private int discountPercent = 10; // 10% 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGarde() == VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
