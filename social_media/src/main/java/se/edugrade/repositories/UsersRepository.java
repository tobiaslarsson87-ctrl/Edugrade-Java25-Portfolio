package se.edugrade.repositories;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.exceptions.DuplicateUsernameException;
import se.edugrade.exceptions.UserNotFoundException;
import java.util.List;
import java.util.Optional;

public class UsersRepository implements RepositoryContract<Users> {
    EntityManagerFactory emf;

    public UsersRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void closeEmf(){
        if(this.emf != null) emf.close();
    }

    @Override
    public Users create(Users addition) {
        if(addition == null) throw new IllegalArgumentException("Non-Existent Users Profiles can't be added to the database");
        if(addition.getUserName() == null) throw new IllegalArgumentException("UserName can't be empty");
        if(addition.getBio() == null) throw new IllegalArgumentException("Biography can't be empty");
        if(addition.getUserName().length() > 25) throw new IllegalArgumentException("Username can't be longer than 25 characters");

        boolean existingUserName = findAll().stream()
                .anyMatch(u -> u.getUserName().equals(addition.getUserName()));

        if(existingUserName){
            throw new DuplicateUsernameException("Username " + addition.getUserName() + " already exist in the database");
        }

        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(addition);
            em.getTransaction().commit();
            return addition;
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("User can't be added to the database: Unknown Reason");
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Users> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be empty.");
        }

        EntityManager em = emf.createEntityManager();
        try {
            Users foundUsers = em.createQuery(
                            "SELECT DISTINCT u FROM Users u " +
                                    "LEFT JOIN FETCH u.followers " +
                                    "LEFT JOIN FETCH u.following f " +
                                    "LEFT JOIN FETCH f.posts " +
                                    "WHERE u.id = :id",
                            Users.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.of(foundUsers);

        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty(); 
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Users> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT u FROM Users u " +
                            "LEFT JOIN FETCH u.followers " +
                            "LEFT JOIN FETCH u.following " +
                            "LEFT JOIN FETCH u.posts",
                    Users.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Users update(Users revision) {
        if(revision == null){
            throw new IllegalArgumentException("Users configuration can't be absent");
        }
        if(revision.getUserName() == null){
            throw new IllegalArgumentException("UserName can't be empty");
        }
        if(revision.getBio() == null){
            throw new IllegalArgumentException("Biography can't be empty");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Users updated = em.merge(revision);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Failed to update Users Profile.");
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        if(id == null){
            throw new IllegalArgumentException("ID can't be non-existent");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Users users = em.find(Users.class, id);
            if(users != null){
                em.remove(users);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Failed to delete user profile");
        } finally {
            em.close();
        }
    }

    public void followTransaction(Long followerId, Long followId){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Users follower = em.find(Users.class, followerId);
            Users followThis = em.find(Users.class, followId);

            if (follower == null || followThis == null) {
                throw new UserNotFoundException("User not found");
            }

            follower.follow(followThis); // För att undvika FUCKING LAZY ERRORS!!!!

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Unable to follow user with ID: " + followId);
        } finally {
            em.close();
        }
    }

    public void unfollowTransaction(Long followerId, Long unfollowId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Users follower = em.find(Users.class, followerId);
            Users unfollowThis = em.find(Users.class, unfollowId);

            if (follower == null || unfollowThis == null) {
                throw new UserNotFoundException("User not found");
            }

            follower.unFollow(unfollowThis); // För att undvika FUCKING LAZY ERRORS!!!!

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Unable to unfollow user with ID: " + unfollowId);
        } finally {
            em.close();
        }
    }

    //PLOCKA FRAM ALLA POSTS SOM ÄR SKRIVNA AV PERSONER SOM INSKICKAD USER ID FÖLJER
    public List<Long> findPostsByFollowers(Long id){
        EntityManager em = emf.createEntityManager();

        try{
            em.getTransaction().begin();
            List<Long> IDs = em.createQuery("""
            SELECT p.id FROM Post p
            JOIN p.author a
            JOIN a.followers f
            WHERE f.id = :id
            ORDER BY p.createdAt DESC
        """, Long.class)
                    .setParameter("id", id)
                    .setMaxResults(50)
                    .getResultList();

            em.getTransaction().commit();
            return IDs;
        } finally {
            em.close();
        }
    }

    //ANVÄND ALLA IDs FÖR ATT PLOCKA FRAM ALLA POSTS OCH FETCHA RELATIONERNA: WORKAROUND, HIBERNATE FÖRBJUDER KOMPLEXA FETCH JOINS I SAMMA TRANSAKTION
    public List<Post> findFriendPostsById(List<Long> ids){
        if(ids.isEmpty()) return List.of();
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("""
                SELECT DISTINCT p FROM Post p
                LEFT JOIN FETCH p.author a
                LEFT JOIN FETCH p.comments c
                LEFT JOIN FETCH p.hashtags h
                LEFT JOIN FETCH p.likes l
                WHERE p.id IN :ids
                """, Post.class)
                    .setParameter("ids", ids)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    //PLOCKA FRAM POSTS SKRIVNA AV EN SPECIFIK ANVÄNDARE
    public List<Post> currentUserPosts(Long id){
        if(id == null) return List.of();
        EntityManager em = emf.createEntityManager();

        try{
            return em.createQuery("""
                SELECT DISTINCT p FROM Post p
                LEFT JOIN FETCH p.author
                LEFT JOIN FETCH p.likes
                LEFT JOIN FETCH p.comments
                LEFT JOIN FETCH p.hashtags
                WHERE p.author.id = :id
        """, Post.class)
                    .setParameter("id", id)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
