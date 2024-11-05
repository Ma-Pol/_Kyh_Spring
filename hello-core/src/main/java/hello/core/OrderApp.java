package hello.core;

import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

import static hello.core.member.Grade.BASIC;
import static hello.core.member.Grade.VIP;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);

        Long memberId2 = 2L;
        Member member2 = new Member(memberId2, "memberB", BASIC);
        memberService.join(member2);

        Order order2 = orderService.createOrder(memberId2, "itemB", 5000);

        System.out.println("order2 = " + order2);
    }
}
