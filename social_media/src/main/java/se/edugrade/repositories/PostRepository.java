package se.edugrade.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.subclasses.ContentType;
import se.edugrade.entities.Post;
import se.edugrade.exceptions.InvalidContentException;
import java.util.List;
import java.util.Optional;

public class PostRepository implements RepositoryContract<Post> {

    private final EntityManagerFactory emf;

    public PostRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Post create(Post post) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(post);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to create a new 'Post'" , e);
        } finally {
            em.close();
        }
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Post.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Post> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p", Post.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Post update(Post post) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Post merged = em.merge(post);
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to update Post: ", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Optional.ofNullable(em.find(Post.class, id))
                    .ifPresent(em::remove);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    public List<Post> findAllOrdered() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Post p ORDER BY p.createdAt DESC",
                    Post.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
    // För backup & service
    public List<Post> findAllWithRelations() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("""
                SELECT DISTINCT p FROM Post p
                LEFT JOIN FETCH p.hashtags
                LEFT JOIN FETCH p.comments
                LEFT JOIN FETCH p.likes
                """, Post.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public Optional<Post> findByIdWithRelations(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Post post = em.createQuery("""
                SELECT p FROM Post p
                LEFT JOIN FETCH p.hashtags
                LEFT JOIN FETCH p.comments
                LEFT JOIN FETCH p.likes
                WHERE p.id = :id
                """, Post.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(post);

        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
