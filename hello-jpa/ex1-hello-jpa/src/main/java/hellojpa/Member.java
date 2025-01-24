package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@Table(name = "MBR") // catalog, schema, uniqueConstraints(DDL) 설정 가능
@Getter
@Setter
//@SequenceGenerator( // @GeneratedValue(strategy = AUTO) 일 때 사용
//        name = "MEMBER_SEQ_GENERATOR",
//        sequenceName = "MEMBER_SEQ",
//        initialValue = 1,
//        allocationSize = 1 // 기본값: 50
//)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // AUTO, IDENTITY, SEQUENCE, TABLE(권장하지 않음)
    private Long id;

    @Column(name = "name") // 컬럼 매핑
    private String username;

    // BigDecimal, BigInteger 타입에 사용
    // - precision: 소수점을 포함한 전체 자리수 설정
    // - scale: 소수의 자리수 설정
    @Column(precision = 100, scale = 0)
    private BigDecimal age;

    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) // 날짜 타입 매핑(구식. Date, Calendar 에만 사용)
    private Date createdDate;

    // LocalDateTime 등 자바 8 이상의 날짜 타입은 기본적으로 자동 매핑
    private LocalDateTime lastModifiedDate;

    @Lob // BLOB, CLOB 매핑
    private String description;

//    @Transient // 특정 필드를 컬럼에 매핑하지 않음(매핑 무시)
}
