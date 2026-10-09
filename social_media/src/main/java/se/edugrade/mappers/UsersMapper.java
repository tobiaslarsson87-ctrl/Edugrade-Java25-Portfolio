package se.edugrade.mappers;
import org.hibernate.Hibernate;
import se.edugrade.dto.UsersDTO;
import se.edugrade.entities.Users;

public class UsersMapper {
    public static UsersDTO DTOMapper(Users u){
        //EXTRA SKYDD: Returnerar bara noll om lazy collection inte är laddad för att undvika error.
        int followersCount = Hibernate.isInitialized(u.getFollowers())
                ? u.getFollowers().size()
                : 0;
        //EXTRA SKYDD: Returnerar bara noll om lazy collection inte är laddad för att undvika error.
        int postsCount = Hibernate.isInitialized(u.getPosts())
                ? u.getPosts().size()
                : 0;
        return new UsersDTO(
                u.getId(),
                u.getUserName(),
                u.getBio(),
                u.getCreatedAt(),
                followersCount,
                postsCount
        );
    }
}
