package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class CascadeMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // =========== //

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Parent parent = new Parent();
            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
//            em.persist(child1); // cascade all 설정하면 필요없음
//            em.persist(child2); // cascade all 설정하면 필요없음

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            Child findChild1 = em.find(Child.class, child1.getId());
            Child findChild2 = em.find(Child.class, child2.getId());

            findParent.getChildList().remove(0); // orphanRemoval = true (delete 쿼리 실행)

            em.flush();
            em.clear();

            Parent refindParent = em.find(Parent.class, parent.getId());
            for (Child child : refindParent.getChildList()) {
                System.out.println("child.getId() = " + child.getId());
            }

            em.remove(refindParent); // Child 전부 삭제
            em.flush();
            em.clear();

            Child refindChild2 = em.find(Child.class, child2.getId()); // null 반환
            System.out.println("refindChild2 = " + refindChild2);

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
