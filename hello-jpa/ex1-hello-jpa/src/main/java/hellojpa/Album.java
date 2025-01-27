package hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A") // item 테이블 dtype 컬럼에 입력될 값을 설정
@Getter
@Setter
public class Album extends Item {
    private String artist;
}
