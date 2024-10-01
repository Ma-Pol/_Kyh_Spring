package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    // SELECT m FROM Member AS m WHERE m.name = ? 으로 자동 쿼리 생성
    @Override
    Optional<Member> findByName(String name);
}
