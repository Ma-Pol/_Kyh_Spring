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
            insertMember(em, 1L, "memberA");
//            findMemberById(em, 1L);
//            updateMember(em, 1L, "MemberJPA");
//            deleteMember(em, 1L);
//            findMembers(em);
//            findMemberByName(em, "memberJPA");

            tx.commit();
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
