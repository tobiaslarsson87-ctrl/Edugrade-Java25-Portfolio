import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.edugrade.dto.UsersDTO;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.UserNotFoundException;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.UsersService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUsersService {
    EntityManagerFactory emf;
    UsersRepository ur;
    UsersService us;

    @BeforeEach
    void init() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        ur = new UsersRepository(emf);
        us = new UsersService(ur);
    }

    @AfterEach
    void close() {
        if (ur != null) ur.closeEmf();
    }

    @Test
    @DisplayName("Testa så att DTO går att plocka fram med ID")
    void testFindById(){
        //given
        Users userEntity = ur.create(new Users("Tobias", "För test"));
        //when
        UsersDTO userDTO = us.findById(userEntity.getId());
        //then
        assertNotNull(userDTO);
        assertEquals(userEntity.getId(),userDTO.id());
        assertEquals(userEntity.getUserName(), userDTO.userName());
    }

    @Test
    @DisplayName("Testa så att UserNotFoundException kastas om det inte finns någon rad för ID")
    void testFindByIdIfNull(){
        //given
        Long id = 999L;
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> us.findById(id), "Exception ska kastas när man söker på tomt ID");
    }

    @Test
    @DisplayName("Testa så att findByName fungerar som det ska")
    void testFindByName(){
        //given
        Users userEntity1 = ur.create(new Users("Tobias1", "För test"));
        Users userEntity2 = ur.create(new Users("Tobias2", "För test"));
        Users userEntity3 = ur.create(new Users("Tobias3", "För test"));
        //when
        List<UsersDTO> findings = us.findByName("Tobias");
        //then
        assertEquals(3, findings.size());
    }

    @Test
    @DisplayName("Testa så att showAll visar rätt antal")
    void testShowAll(){
        //given
        Users userEntity1 = ur.create(new Users("Tobias1", "För test"));
        Users userEntity2 = ur.create(new Users("Tobias2", "För test"));
        Users userEntity3 = ur.create(new Users("Tobias3", "För test"));
        //when
        List<UsersDTO> allUsers = us.showAll();
        //then
        assertEquals(3, allUsers.size());
    }
}