package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JPQL2Main {
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
            member.setUsername("memberA");
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

    private static void bulkOperation(EntityManager em) {
        // 벌크 연산
        // 단일 대상이 아닌, 엔티티 전체를 대상으로 하는 Update, Delete 쿼리
        em.createQuery("UPDATE Member m SET m.age = :memberAge")
                .setParameter("memberAge", 10)
                .executeUpdate(); // <- 벌크 연산. 영향을 받은 row 수를 반환

        // 주의!
        // 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리함
        // => 1. 항상 벌크 연산을 먼저 수행하는 것을 권장
        // => 2. 벌크 연산 후 영속성 컨텍스트를 초기화하는 것을 권장(경우에 따라 재조회까지)
    }

    private static void namedQuery(EntityManager em, Member member) {
        // Named 쿼리
        // - 쿼리를 미리 작성해두고 이름을 지정해 재활용하는 것(정적 쿼리만 가능)
        // - 해당 엔티티의 어노테이션, 또는 XML 에 정의(XML 의 우선도가 더 높음)
        //    - XML 정의 방법: META-INF 내 persistence.xml, ormMember.xml 참조
        // - 어플리케이션 로딩 시점에 초기화 후 재사용(SQL 로 파싱 후 캐싱)
        // - 중요: 어플리케이션 로딩 시점에 쿼리 검증
        em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", member.getUsername())
                .getResultList();
        em.createNamedQuery("Member.count", Integer.class)
                .getResultList();
    }

    private static void useEntityDirect(EntityManager em, Member member, Team team) {
        // 엔티티 직접 사용

        // 기본키 값: 엔티티를 직접 사용하면 SQL 에서 해당 엔티티의 primitive key 를 사용한다.
        // 예1
        em.createQuery("SELECT COUNT(m) FROM Member m", Integer.class)
                .getSingleResult();
        em.createQuery("SELECT COUNT(m.id) FROM Member m", Integer.class) // 위 쿼리와 동일
                .getSingleResult();
        // 예2
        em.createQuery("SELECT m FROM Member m WHERE m = :member", Member.class)
                .setParameter("member", member)
                .getResultList();
        em.createQuery("SELECT m FROM Member m WHERE m.id = :memberId", Member.class) // 위 쿼리와 동일
                .setParameter("memberId", member.getId())
                .getResultList();

        // 외래키 값: 기본키 값과 같은 방식으로 대상 엔티티의 primitive key(본 엔티티의 foreign key)를 사용한다.
        // 예
        em.createQuery("SELECT m FROM Member m WHERE m.team = :team")
                .setParameter("team", team)
                .getResultList();
        em.createQuery("SELECT m FROM Member m WHERE m.team.id = :teamId") // 위 쿼리와 동일
                .setParameter("teamId", team.getId())
                .getResultList();
    }

    private static void polymorphismQuery(EntityManager em) {
        // 다형성 쿼리
        // TYPE(): Item 중 자식인 Book, Movie 만 조회하기
        em.createQuery("SELECT i FROM Item i WHERE TYPE(i) IN (Book, Movie)", Item.class)
                .getResultList();

        // TREAT(): 부모인 Item 과 자식 Book 이 있을 때
        em.createQuery("SELECT i FROM Item i WHERE TREAT(i as Book).author = 'kim'", Item.class)
                .getResultList();
    }

    private static void fetchJoin(EntityManager em) {
        // 페치 조인(fetch join)
        // SQL 조인의 일종이 아님. JPQL 에서 성능 최적화를 위해 제공하는 기능
        // 연관된 엔티티나 컬렉션을 한 번의 SQL 로 조회하는 기능
        Team team1 = new Team();
        team1.setName("team1");
        Team team2 = new Team();
        team2.setName("team2");
        Team team3 = new Team();
        team3.setName("team3");

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        member1.setTeam(team1);
        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(21);
        member2.setTeam(team1);
        Member member3 = new Member();
        member3.setUsername("member3");
        member3.setAge(22);
        member3.setTeam(team2);
        Member member4 = new Member();
        member4.setUsername("member4");
        member4.setAge(23);

        em.persist(team1);
        em.persist(team2);
        em.persist(team3);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.flush();
        em.clear();

        // ManyToOne, OneToOne 페치 조인
        List<Member> result1 = em.createQuery("SELECT m FROM Member m JOIN FETCH m.team", Member.class)
                .getResultList(); // 즉시 로딩과 같은 효과. 그러나 명시적으로 설정하는 것임에 차이가 있음
        for (Member result : result1) {
            System.out.println("result = " + result);
        }

        em.clear();

        // OneToMany, ManyToMany 페치 조인
        List<Team> result2 = em.createQuery("SELECT t FROM Team t JOIN FETCH t.members", Team.class)
                .getResultList();
        for (Team result : result2) {
            System.out.println("result = " + result);
            System.out.println("result.members = " + result.members);
        }

        // 페치 조인의 한계
        // 1. 페치 조인 대상에는 별칭을 줄 수 없다. (하이버네이트 자체적으로는 가능하지만 가급적 사용 X)
        // 2. 둘 이상의 컬렉션은 페치 조인할 수 없다.
        // 3. 컬렉션을 페치 조인하면 페이징 API 를 사용할 수 없다. (setFirstResult, setMaxResults)
        //    단,일대일, 다대일과 같은 단일 값 연관 필드에서는 페이징 가능
    }

    private static void pathExpression(EntityManager em) {
        // 경로표현식
        // 상태 필드: 경로 탐색의 끝, 이후의 탐색 X
        List<String> result1 = em.createQuery("SELECT m.username FROM Member m", String.class)
                .getResultList();
        System.out.println("result1 = " + result1);

        // 단일 값 연관 경로: 묵시적 내부 조인(inner join) 발생, 이후의 탐색 O
        List<String> result2 = em.createQuery("SELECT m.team.name FROM Member m", String.class) // m.team 이후 탐색으로 name 조회
                .getResultList();
        System.out.println("result2 = " + result2);

        // 컬렉션 값 연관 경로: 묵시적 내부 조인 발생, 이후의 탐색 X => 컬렉션이기 때문
        List<Member> result3 = em.createQuery("SELECT t.members FROM Team t", Member.class)
                .getResultList();
        System.out.println("result3 = " + result3.get(0).getUsername());

        // 묵시적 조인은 사용하지 말 것. 항상 명시적 조인을 사용하기
        // 묵시적 조인은 항상 내부 조인
    }
}