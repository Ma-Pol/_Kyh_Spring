package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // =========== //

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("userA");
            member.setCreatedBy("KIM");
            member.setCreatedDate(LocalDateTime.now());

            Team team = new Team();
            team.setName("teamA");
            team.getMembers().add(member);

            em.persist(member);
            em.persist(team);

            em.flush();
            em.clear();

/*
//            Member findMember = em.find(Member.class, member.getId());
            Member findMember = em.getReference(Member.class, member.getId()); // 껍데기만 가져옴. 실제 데이터는 나중에 값을 호출할 때 가져옴.
            System.out.println("findMember.getClass() = " + findMember.getClass()); // proxy 객체
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(findMember)); // 프록시 인스턴스의 초기화 여부(false)
            Hibernate.initialize(findMember);
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(findMember)); // true

            em.detach(findMember); // 준영속 상태로 전환
            System.out.println("findMember = " + findMember.getUsername()); // LazyInitializationException
*/

            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.getTeam().getClass() = " + findMember.getTeam().getClass()); // 지연로딩: proxy 객체

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
}
