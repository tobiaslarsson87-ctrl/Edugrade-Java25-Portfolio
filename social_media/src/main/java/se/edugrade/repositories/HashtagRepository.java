package se.edugrade.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import se.edugrade.entities.Hashtag;
import se.edugrade.exceptions.InvalidContentException;
import java.util.List;
import java.util.Optional;

public class HashtagRepository implements RepositoryContract<Hashtag>{
    private EntityManagerFactory emf;

    public HashtagRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public void closeEmf(){
        if(this.emf !=null) emf.close();
    }

    // Skapar en ny Hashtag till db
    @Override
    public Hashtag create(Hashtag hashtag) {
        if (hashtag == null ) throw new IllegalArgumentException("Non-existing Hashtag can be added");
        if(hashtag.getTag() == null) throw new IllegalArgumentException("'Tag' can not be empty");
        EntityManager em = emf.createEntityManager();

        Hashtag exist = findByTag(hashtag.getTag()); // Creatar en ny om den redan inte finns i db
        if(exist != null){
            return exist;
        }
        try{
            em.getTransaction().begin();
            em.persist(hashtag);
            em.getTransaction().commit();
        } catch (Exception e){
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to create a new 'Hashtag'" + e.getMessage());
        } finally {
            em.close();
        }
        return hashtag;
    }
    // Hittar Hashtag med HashtagId i db
    @Override
    public Optional<Hashtag> findById(Long id) {
        if (id == null) throw new InvalidContentException("Hashtag id can not be null");

        EntityManager em = emf.createEntityManager();
        try{
            Hashtag found = em.find(Hashtag.class,id);
            return Optional.ofNullable(found);
        }finally {
            em.close();
        }
    }
    // Hittar alla Hashtags i db
    @Override
    public  List<Hashtag> findAll() {
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT h FROM Hashtag h ", Hashtag.class).getResultList();
        }
        finally {
            em.close();
        }
    }
    // Uppdaterar befintlig hashtag i db
    @Override
    public Hashtag update(Hashtag hashtag) {
        if (hashtag == null) throw new IllegalArgumentException("Can not be null");
        EntityManager em = emf.createEntityManager();
        if (hashtag == null){
            throw new InvalidContentException("Hashtag can not be null");
        }
        if (hashtag.getId() == null){
            throw new InvalidContentException("Hashtag id cannot be null for update");
        }

        try{
            em.getTransaction().begin();
            em.merge(hashtag);
            em.getTransaction().commit();
        } catch (Exception e){
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new InvalidContentException("Failed to update Hashtag" +e.getMessage());
        }finally {
            em.close();
        }
        return hashtag;
    }
    // Raderar en Hashtag i db med hjälp av HashtagId
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new InvalidContentException("Hashtag id can not be null");
        }
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Hashtag hashtag = em.find(Hashtag.class, id);
            if(hashtag != null){
                em.remove(hashtag);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    // För backup
    public List<Hashtag> findAllWithPosts() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("""
                SELECT DISTINCT h FROM Hashtag h
                LEFT JOIN FETCH h.post
                """, Hashtag.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Hashtag findByTag (String tag){
        if (tag == null) throw new InvalidContentException("Tag can be null");

        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT h FROM Hashtag h WHERE h.tag = :tag ", Hashtag.class)
                    .setParameter("tag", tag)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        } finally {
            em.close();
        }
    }
}
