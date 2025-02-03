package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // =========== //

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            persistMembers(em);
//            em.flush();

            LocalDateTime searchStartDateTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
            LocalDateTime searchEndDateTime = LocalDateTime.of(2024, 12, 1, 0, 0, 0);

            // JPQL
            jpqlSelect(em, searchStartDateTime, searchEndDateTime);

            // JPA Criteria
            jpaCriteriaSelect(em, searchStartDateTime, searchEndDateTime);

            // Native Query
            nativeQuerySelect(em, searchStartDateTime, searchEndDateTime);

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

    private static void jpqlSelect(EntityManager em, LocalDateTime searchStartDateTime, LocalDateTime searchEndDateTime) {
        System.out.println("\n============== JPQL ==============");
        List<Member> members1 = em.createQuery("SELECT m FROM Member m WHERE m.username LIKE :username", Member.class)
                .setParameter("username", "%A%")
                .getResultList();
        for (Member member : members1) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }
        System.out.println();
        List<Member> members2 = em.createQuery("SELECT m FROM Member m WHERE m.createdDate BETWEEN :startDate AND :endDate", Member.class)
                .setParameter("startDate", searchStartDateTime)
                .setParameter("endDate", searchEndDateTime)
                .getResultList();
        for (Member member : members2) {
            System.out.println("member.getCreatedDate() = " + member.getCreatedDate());
        }
    }

    private static void jpaCriteriaSelect(EntityManager em, LocalDateTime searchStartDateTime, LocalDateTime searchEndDateTime) {
        System.out.println("\n============== JPA Criteria ==============");
        // Criteria 사용 준비
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);

        // 조회 클래스 지정
        Root<Member> m = query.from(Member.class);

        CriteriaQuery<Member> cq1 = query.select(m).where(cb.like(m.get("username"), "%A%"));
        List<Member> members1 = em.createQuery(cq1).getResultList();
        for (Member member : members1) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }
        System.out.println();
        CriteriaQuery<Member> cq2 = query.select(m).where(cb.between(m.get("createdDate"), searchStartDateTime, searchEndDateTime));
        List<Member> members2 = em.createQuery(cq2).getResultList();
        for (Member member : members2) {
            System.out.println("member.getCreatedDate() = " + member.getCreatedDate());
        }
    }

    private static void nativeQuerySelect(EntityManager em, LocalDateTime searchStartDateTime, LocalDateTime searchEndDateTime) {
        System.out.println("\n============== Native Query ==============");
        String nativeQuery1 = "SELECT * FROM member WHERE username LIKE :username";
        List<Member> members = em.createNativeQuery(nativeQuery1, Member.class)
                .setParameter("username", "%A%")
                .getResultList();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }
        System.out.println();
        String nativeQuery2 = "SELECT * FROM member WHERE createdDate BETWEEN :startDate AND :endDate";
        List<Member> members2 = em.createNativeQuery(nativeQuery2, Member.class)
                .setParameter("startDate", searchStartDateTime)
                .setParameter("endDate", searchEndDateTime)
                .getResultList();
        for (Member member : members2) {
            System.out.println("member.getCreatedDate() = " + member.getCreatedDate());
        }
    }

    private static void persistMembers(EntityManager em) {
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();

        member1.setUsername("memAber");
        member2.setUsername("memberA");
        member3.setUsername("Amember");
        member4.setUsername("memberXXX");

        member1.setCreatedDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0));
        member2.setCreatedDate(LocalDateTime.of(2024, 12, 1, 0, 0, 0));
        member3.setCreatedDate(LocalDateTime.of(2025, 1, 1, 0, 0, 0));
        member4.setCreatedDate(LocalDateTime.of(2025, 12, 1, 0, 0, 0));

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
}
