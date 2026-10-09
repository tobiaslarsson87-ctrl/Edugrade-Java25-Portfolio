package se.edugrade.mappers;
import se.edugrade.dto.SimpleUsersDTO;
import se.edugrade.entities.Users;

public class SimpleUsersMapper {
    public static SimpleUsersDTO simpleMap(Users u){
        return new SimpleUsersDTO(u.getId(), u.getUserName());
    }
}
