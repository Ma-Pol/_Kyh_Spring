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
            Address address = new Address("city", "street", "zipcode");

            Member member1 = new Member();
            member1.setUsername("MemberA");
            member1.setHomeAddress(address);
            member1.setWorkPeriod(new Period(LocalDateTime.MIN, LocalDateTime.now()));

            Member member2 = new Member();
            member2.setUsername("MemberB");
//            member2.setHomeAddress(address);
            member2.setHomeAddress(Address.getCopyAddress(address));
            member2.setWorkPeriod(new Period(LocalDateTime.MIN, LocalDateTime.now()));

            em.persist(member1);
            em.persist(member2);

            // 같은 Address 객체를 사용하면 member2의 city 까지 변경됨
            // 새로운 Address 객체를 만들어서 사용하면 방지할 수 있음
            // 또는 setHomeAddress 내에서 새로운 객체를 만들어 대입하도록 할 수도 있음
            // => 불변 객체
            member1.getHomeAddress().setCity("newCity");

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
