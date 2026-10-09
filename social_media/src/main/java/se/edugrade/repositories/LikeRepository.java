package se.edugrade.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.entities.Like;
import java.util.List;
import java.util.Optional;


public class LikeRepository implements RepositoryContract<Like> {

    private EntityManagerFactory emf;
    public LikeRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //------------------------------------
    //CREATE – sparar en ny Like i databasen med transaktionshantering
    //------------------------------------
    @Override
    public Like create(Like like) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin(); // Startar transaktion
            em.persist(like); //Lägger till like i databasen
            em.getTransaction().commit(); // Sparar ändringar
            return like;

            //Om något går fel, backa transaktionen helt.
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e; //Skickar felet vidare.
        }
        finally {
            em.close(); // Stänger kopplingen till databasen
        }
    }
    //------------------------------------
    // READ – hämtar en Like baserat på ID. Returnerar Optional för att undvika null-problem.
    //------------------------------------
    @Override
    public Optional<Like> findById(Long id) {
        EntityManager em =  emf.createEntityManager();

        try {
            Like found = em.find(Like.class, id); // Hämtar Like eller null
            return Optional.ofNullable(found); // Wrappa i Optional
        } finally {
            em.close();
        }
    }

    //------------------------------------
    //READ - hämtar alla Likes från databasen.
    //------------------------------------

    @Override
    public List<Like> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            // En enkel JPQL-query som hämtar alla Likes
            return em.createQuery("SELECT l from Like l", Like.class).getResultList();
        } finally
        {
            em.close();
        }
    }

    //------------------------------------
    //Update
    //------------------------------------
    @Override
    public Like update(Like like) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Like updated = em.merge(like);  // Slår ihop ändringar(mergar) med databasen
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    //------------------------------------
    // DELETE – tar bort en Like baserat på dess ID.
    //------------------------------------
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Like found = em.find(Like.class, id);
            if (found != null) {
                em.remove(found); //Radera om den hittades
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

//------------------------------------
// Avancerad READ-query - Hämtar alla likes fö en specefik post
//------------------------------------
public List<Like> findByPostId(Long postId) {
    EntityManager em = emf.createEntityManager();

    try {
        return em.createQuery(
                "SELECT l FROM Like l WHERE l.post.id = :postId",
                Like.class
                )
                .setParameter("postId", postId)
                .getResultList();
    } finally {
        em.close();
    }
}

//------------------------------------
// Kontroll/validerings-query – kollar om en användare redan likeat en specifik post
//------------------------------------
public boolean existsByUserAndPost(Long userId, Long postId) {
        EntityManager em = emf.createEntityManager();

        try {
            Long count = em.createQuery(
                    "SELECT COUNT(l) FROM Like l WHERE l.users.id = :userId AND l.post.id = :postId",
                    Long.class
            )
                    .setParameter("userId", userId)
                    .setParameter("postId", postId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }


    //------------------------------------
    // Statistik/aggregat-query - Räknar hur många likes en post har (för statistik och UI)
    //------------------------------------
    public long countLikesForPost(Long postId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId",
                            Long.class
                    )
                    .setParameter("postId", postId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}