package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JPQL1Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // =========== //

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setMemberType(MemberType.ADMIN);
            member.setTeam(team);

            em.persist(member);
            em.persist(team);
            em.flush();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        // =========== //
        emf.close();
    }

    private static void basicFunction(EntityManager em) {
        // JPQL 기본 함수
        // CONCAT, SUBSTRING, TRIM, LOWER, UPPER, LENGTH, LOCATE, ABS, SQRT, MOD, SIZE, INDEX 지원
        List<String> result1 = em.createQuery("SELECT CONCAT(m.username, m.team.name) FROM Member m", String.class)
                .getResultList();
        for (String s1 : result1) {
            System.out.println("s1 = " + s1);
        }

        // 사용자 정의 함수 호출
        // 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록하여 사용
        // dialect 패키지, persistence.xml 참조
        List<String> result2 = em.createQuery("SELECT FUNCTION('GROUP_CONCAT', m.username) FROM Member m", String.class)
                .getResultList();
        for (String s2 : result2) {
            System.out.println("s2 = " + s2);
        }

    }

    private static void conditional(EntityManager em) {
        // 조건식
        // 기본 CASE 식
        List<String> result1 = em.createQuery("SELECT" +
                        " CASE" +
                        " WHEN m.age <= 10 THEN '학생요금'" +
                        " WHEN m.age >= 60 THEN '경로요금'" +
                        " ELSE '일반요금'" +
                        " END" +
                        " FROM Member m", String.class)
                .getResultList();
        System.out.println("result1 = " + result1.get(0));

        // 단순 CASE 식
        List<String> result2 = em.createQuery("SELECT" +
                        " CASE t.name" +
                        " WHEN 'teamA' THEN '인센티브 110%'" +
                        " WHEN 'teamB' THEN '인센티브 120%'" +
                        " ELSE '인센티브 105%'" +
                        " END" +
                        " FROM Team t", String.class)
                .getResultList();
        System.out.println("result2 = " + result2.get(0));

        // COALESCE: 하나씩 조회 후 null 이 아니면 기본값 반환
        List<String> result3 = em.createQuery("SELECT COALESCE(m.username, '무명 회원') FROM Member m", String.class)
                .getResultList();
        System.out.println("result3 = " + result3.get(0));

        // NULLIF: 두 값이 같으면 null 반환, 다르면 첫 번째 값 반환
        List<String> result4 = em.createQuery("SELECT NULLIF(m.username, '관리자') FROM Member m", String.class)
                .getResultList();
        System.out.println("result4 = " + result4.get(0));
    }

    private static void type(EntityManager em) {
        // JPQL 타입 표현
        // ENUM: 패키지명 포함 필요
        List<Object[]> result = em.createQuery("SELECT m.username, 'HELLO', TRUE FROM Member m WHERE m.memberType = hellojpa.MemberType.ADMIN")
                .getResultList();
        for (Object[] objects : result) {
            System.out.println("objects[0] = " + objects[0]); // m.username
            System.out.println("objects[1] = " + objects[1]); // HELLO
            System.out.println("objects[2] = " + objects[2]); // true
        }
    }

    private static void subquery(EntityManager em) {
        // 서브쿼리
        // 예) 나이가 평균보다 많은 회원
        em.createQuery("SELECT m FROM Member m WHERE m.age > (SELECT AVG(m2.age) FROM Member m2)", Member.class)
                .getResultList();

        // 서브 쿼리 지원 함수 1
        // EXISTS: 서브 쿼리 내 결과가 존재하면 참
        // 예) teamA 팀 소속인 회원
        em.createQuery("SELECT m FROM Member m WHERE EXISTS(SELECT t FROM m.team t WHERE t.name = :teamName)", Member.class)
                .setParameter("teamName", "teamA")
                .getResultList();

        // 서브 쿼리 지원 함수 2
        // ALL: 서브 쿼리 내 모든 조건을 만족하면 참
        // 예) 전체 상품 각각의 재고보다 주문량이 많은 주문들
        em.createQuery("SELECT o FROM Order o WHERE o.orderAmount > ALL(SELECT p.stockAmount FROM Product p)", Order.class)
                .getResultList();

        // 서브 쿼리 지원 함수 3
        // ANY, SOME: 서브 쿼리 내 조건을 하나라도 만족하면 참
        // 에) 어떤 팀이든, 팀에 소속된 회원들
        em.createQuery("SELECT m FROM Member m WHERE m.team = ANY(SELECT t FROM Team t)", Member.class)
                .getResultList();

        // JPA 서브 쿼리의 한계
        // WHERE, HAVING 절에서만 서브 쿼리 사용 가능
        // 단, SELECT 절은 하이버네이트의 지원으로 사용 가능
        // FROM 절의 서브 쿼리는 현재 JPQL 에서는 불가능 -> JOIN 으로 해결할 것(안되면 네이티브 쿼리 사용)
    }

    private static void join(EntityManager em) {
        // 내부 조인
        em.createQuery("SELECT m FROM Member m INNER JOIN m.team t", Member.class)
                .getResultList();

        // 외부 조인
        em.createQuery("SELECT m FROM Member m LEFT JOIN m.team t", Member.class)
                .getResultList();

        // 세타 조인
        em.createQuery("SELECT m FROM Member m, Team t WHERE m.username = t.name", Member.class)
                .getResultList();

        // 조인 대상 필터링(ON 절)
        em.createQuery("SELECT m FROM Member m LEFT JOIN m.team t ON t.name = :teamName", Member.class)
                .setParameter("teamName", "teamA")
                .getResultList();

        // 연관관계가 없는 엔티티 외부 조인
        em.createQuery("SELECT m FROM Member m LEFT JOIN Team t ON m.username = t.name", Member.class)
                .getResultList();
    }

    private static void paging(EntityManager em) {
        // 페이징
        for (int i = 0; i < 100; i++) {
            Member member = new Member();
            member.setUsername("member" + i);
            member.setAge(10 + i);
            em.persist(member);
        }

        for (int i = 0; i < 100; i += 10) {
            System.out.println("\n================ PAGE " + (i / 10 + 1) + " ================");
            List<Member> members = em.createQuery("SELECT m FROM Member m ORDER BY m.age DESC", Member.class)
                    .setFirstResult(i)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("members.size() = " + members.size());
            for (Member member : members) {
                System.out.println("member = " + member);
            }
        }
    }

    private static void projection(EntityManager em) {
        // 프로젝션: SELECT 절에 조회할 대상을 지정하는 것
        // 엔티티 프로젝션
        List<Member> result1 = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();

        // 엔티티 프로젝션
        List<Team> result2 = em.createQuery("SELECT m.team FROM Member m", Team.class)
                .getResultList();

        // 임베디드 타입 프로젝션
        List<Address> result3 = em.createQuery("SELECT o.address FROM Order o", Address.class)
                .getResultList();


        // 스칼라 타입 프로젝션
        // 문제점: 반환 타입을 어떻게 지정해야 하는가?
        List result4 = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m")
                .getResultList();

        // 스칼라 타입 프로젝션 반환 타입 해결책 1
        List<Object[]> scalaResult1 = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m")
                .getResultList();
        Object[] scalaResult = scalaResult1.get(0);
        System.out.println("username = " + scalaResult[0]);
        System.out.println("age = " + scalaResult[1]);

        // 스칼라 타입 프로젝션 반환 타입 해결책 2
        List<MemberDTO> scalaResult2 = em.createQuery("SELECT DISTINCT new hellojpa.MemberDTO(m.username, m.age) FROM Member m", MemberDTO.class)
                .getResultList();
        MemberDTO memberDTO = scalaResult2.get(0);
        System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
        System.out.println("memberDTO.getAge() = " + memberDTO.getAge());
    }

    private static void parameterBinding(EntityManager em) {
        // 파라미터 바인딩 1(권장): 이름 기준
        Member result1 = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        // 파라미터 바인딩 2(비권장): 위치 기준
        Member result2 = em.createQuery("SELECT m FROM Member m WHERE m.username = ?1", Member.class)
                .setParameter(1, "member1")
                .getSingleResult();
    }

    private static void getResult(EntityManager em) {
        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.id = 1", Member.class);

        // 결과 조회
        Member member = em.find(Member.class, 1L);

        // 결과가 하나 이상일 때, 리스트 반환. 결과가 없으면 빈 리스트 반환
        List<Member> members = query.getResultList();

        // 결과가 정확히 하나일 때, 단일 객체 반환
        // 결과가 없으면 jakarta.persistence.NoResultException
        // 둘 이상이면 jakarta.persistence.NonUniqueResultException
        // 단, SpringDataJPA 사용 시, Optional 객체 반환 처리
        Member singleResult = query.getSingleResult();
    }

    private static void typeQuery(EntityManager em) {
        // Type Query
        TypedQuery<Member> typeQuery1 = em.createQuery("SELECT m FROM Member m", Member.class);
        TypedQuery<String> typeQuery2 = em.createQuery("SELECT m.username, m.age FROM Member m", String.class);

        // Query
        Query query = em.createQuery("SELECT m.username, m.age FROM Member m");
    }
}