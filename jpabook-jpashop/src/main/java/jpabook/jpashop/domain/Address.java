package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class Address { // VO
    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }
}
