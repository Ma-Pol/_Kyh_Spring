package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // =========== //

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속 (new / transient)
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속 (managed)
            System.out.println("===== BEFORE =====");
            em.persist(member); // 여기서 DB 쿼리가 실행되지 않음(단, 쿼리 생성 후 저장소에 보관함)
            System.out.println("===== AFTER =====");

            // 준영속 (detached)
//            em.detach(member);

            // 삭제 (removed) (영속성 컨텍스트에서 객체를 삭제 -> DB 에서 삭제)
//            em.remove(member);

            Member findMember = em.find(Member.class, 100L); // select 쿼리가 실행되지 않음(1차 캐시 사용)
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            // 영속성 엔티티의 동일성 보장(1차 캐시 활용)
            Member findMember2 = em.find(Member.class, 100L);
            System.out.println("member1 == member2: " + (findMember == findMember2));

            findMember.setName("Updated");

            tx.commit(); // 여기서 저장소에 보관해놓은 DB 쿼리가 실행됨
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        // =========== //
        emf.close();
    }

    private static void insertMember(EntityManager em, Long memberId, String memberName) {
        Member member = new Member();
        member.setId(memberId);
        member.setName(memberName);

        em.persist(member);
    }

    private static void findMemberById(EntityManager em, Long memberId) {
        Member findMember = em.find(Member.class, memberId);
        System.out.println("findMember.getId() = " + findMember.getId());
        System.out.println("findMember.getName() = " + findMember.getName());
    }

    private static void updateMember(EntityManager em, Long memberId, String memberName) {
        Member findMember = em.find(Member.class, memberId);
        findMember.setName(memberName);
    }

    private static void deleteMember(EntityManager em, Long memberId) {
        Member findMember = em.find(Member.class, memberId);
        em.remove(findMember);
    }

    private static void findMembers(EntityManager em) {
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member.getId() = " + member.getId());
            System.out.println("member.getName() = " + member.getName());
        }
    }

    private static void findMemberByName(EntityManager em, String memberName) {
        List<Member> members = em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class)
                .setParameter("name", memberName)
                .getResultList();

        for (Member member : members) {
            System.out.println("member.getId() = " + member.getId());
            System.out.println("member.getName() = " + member.getName());
        }
    }
}
