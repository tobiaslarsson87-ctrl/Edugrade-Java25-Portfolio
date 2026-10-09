package se.edugrade.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.entities.Comment;
import se.edugrade.entities.Post;
import se.edugrade.exceptions.InvalidContentException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Repository för Comment-entiteten.
 * Implementerar RepositoryContract<Comment> och hanterar CRUD-operationer.
 */
public class CommentRepository implements RepositoryContract<Comment> {
    private final EntityManagerFactory emf;
    public CommentRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Skapar och sparar en ny Comment i databasen.
     * @param comment - kommentaren som ska sparas
     * @return den sparade kommentaren
     */
    @Override
    public Comment create(Comment comment) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
            return comment;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to create Comment", e);
        } finally {
            em.close();
        }
    }
    /**
     * Hämtar en Comment baserat på dess id.
     * @param id - primärnyckeln
     * @return Optional<Comment> - kan vara tom.
     */
    @Override
    public Optional<Comment> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Comment found = em.find(Comment.class, id);
            return Optional.ofNullable(found);
        } finally {
            em.close();
        }
    }
    /**
     * Hämtar alla kommentarer från databasen.
     *
     * @return en lista med alla Comment-objekt
     */
    @Override
    public List<Comment> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comment c", Comment.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    /**
     * Hämtar alla kommentarer kopplade till en specifik post,
     * sorterade i kronologisk ordning (äldst först).
     *
     * @param post posten vars kommentarer ska hämtas
     * @return sorterad lista med kommentarer
     * @throws InvalidContentException om post är null
     */
    public List<Comment> findAllCommentsOnPost(Post post) {
        return Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"))
                .getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .toList();
    }
    /**
     * Uppdaterar en befintlig Comment.
     * @param comment - kommentaren som ska uppdateras
     * @return den uppdaterade kommentaren
     */
    @Override
    public Comment update(Comment comment) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Comment merged = em.merge(comment);
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to update comment", e);
        } finally {
            em.close();
        }
    }
    /**
     * Tar bort en Comment baserat på id.
     * fick lägga till en em.flush för att den alltid direkt ska
     * uppdatera databasen och inte cache:a något.
     * @param id - PK
     */
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Optional.ofNullable(em.find(Comment.class, id))
                    .ifPresent(comment -> {
                        Optional.ofNullable(comment.getPost())
                                .ifPresent(post -> post.getComments().remove(comment));
                        em.remove(comment);
                    });
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to delete comment", e);
        } finally {
            em.close();
        }
    }
}

