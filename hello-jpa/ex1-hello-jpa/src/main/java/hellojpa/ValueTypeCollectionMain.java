package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class ValueTypeCollectionMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // =========== //

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("city1", "street1", "zipcode1"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("돈까스");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("city0", "street0", "zipcode0"));
            member.getAddressHistory().add(new Address("city99", "street99", "zipcode99"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("================== SELECT START ==================");
            Member findMember = em.find(Member.class, member.getId());

            System.out.println("=== ADDRESS ===");
            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }

            System.out.println("=== FOODS ===");
            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            System.out.println("================== UPDATE START ==================");
            // 치킨 -> 한식
            Set<String> findFavoriteFoods = findMember.getFavoriteFoods();
            findFavoriteFoods.remove("치킨");
            findFavoriteFoods.add("한식");
            em.flush();

            // findMember 와 연관된 addressHistory 가 모두 삭제된 후 새로 추가됨
            List<Address> findAddressHistory = findMember.getAddressHistory();
            findAddressHistory.remove(new Address("city0", "street0", "zipcode0"));
            findAddressHistory.add(new Address("city2", "street2", "zipcode2"));
            em.flush();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        // =========== //
        emf.close();
    }
}
