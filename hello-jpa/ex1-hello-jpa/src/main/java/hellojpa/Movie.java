package hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M") // item 테이블 dtype 컬럼에 입력될 값을 설정
@Getter
@Setter
public class Movie extends Item {
    private String director;
    private String actor;
}
