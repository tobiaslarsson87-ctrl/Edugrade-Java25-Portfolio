import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.DuplicateUsernameException;
import se.edugrade.repositories.UsersRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUsers {
    EntityManagerFactory emf;
    UsersRepository ur;

    @BeforeEach
    void init(){
        emf = Persistence.createEntityManagerFactory("TestPU");
        ur = new UsersRepository(emf);
    }

    @AfterEach
    void close(){
        if(ur != null) ur.closeEmf();
    }

    //Testa Interface Metoder. VIKTIGAST
    @Test
    @DisplayName("Kontrollera så att ny användare sparas i databasen och får ett ID")
    void testCreate(){
        //given
        Users preCreate = new Users("Tobias", "Testa med bio");
        int usersCount = 1;
        //when
        Users postCreate = ur.create(preCreate);
        //then
        assertNotNull(postCreate.getId(), "Kontrollera så att managed har fått ett ID");
        assertEquals(usersCount, ur.findAll().size(), "Kontrollera så att enbart en användare finns");
    }
    @Test
    @DisplayName("findById ska returnera en User om det existerar på ID och managed ska spara samma data som det som skickas till DB")
    void testFintById(){
        //given
        Users user = new Users("Tobias", "Testa med bio");
        Users managed = ur.create(user);
        Long id = managed.getId();
        //when
        Optional<Users> found = ur.findById(id);
        //then
        assertTrue(found.isPresent(), "Kontrollera så Optional är fylld med en User");
        assertEquals(managed.getId(), found.get().getId(), "ID ska matcha mellan managed och det som sparas i DB");
        assertEquals("Tobias", found.get().getUserName(), "UserName ska matcha mellan managed och det som sparas i DB");
        assertEquals("Testa med bio", found.get().getBio(), "Biography ska matcha mellan managed och det som sparas i DB");
    }
    @Test
    @DisplayName("findById ska returnera en tom Optional om det INTE existerar på ID och managed ska innehålla samma data som raden i DB")
    void testFindByIdNullable(){
        //given
        Long id = 9999L;
        //when
        Optional<Users> found = ur.findById(id);
        //then
        assertTrue(found.isEmpty(), "Kontrollera så att found är en tom Optional. ");
    }
    @Test
    @DisplayName("Kontrollera så att findAll() returnerar rätt mängd element")
    void testFindAll(){
        //given
        Users managed1 = ur.create(new Users("Tobias", "HEJ"));
        Users managed2 = ur.create(new Users("Albin", "HEJ"));
        Users managed3 = ur.create(new Users("Elin", "HEJ"));
        //when
        List<Users> users = ur.findAll();
        //then
        assertEquals(3, users.size(), "Kontrollera så att storleken matchar");
    }
    @Test
    @DisplayName("update() ska updatera den befintliga användaren")
    void testUpdate(){
        //given
        Users preUpdate = ur.create(new Users("Tobias", "Jag är 38 år gammal"));
        //when
        preUpdate.setUserName("TobiasUltra");
        preUpdate.setBio("Jag är 9999 år gammal");
        Users postUpdate = ur.update(preUpdate);
        //then
        assertEquals("TobiasUltra", postUpdate.getUserName());
        assertEquals("Jag är 9999 år gammal", postUpdate.getBio());
        assertEquals(preUpdate.getId(), postUpdate.getId(), "För att kontrollera att uppdatering sker på samma rad i DB");
    }
    @Test
    @DisplayName("Kontrollera så att delete sker på rätt ID")
    void testDelete(){
        //given
        Users preDelete = ur.create(new Users("Tobias", "Jag ska plockas bort"));
        Long id = preDelete.getId();
        assertEquals(1, ur.findAll().size(), "Kontrollera så att rad existerar innan delete sker");
        //when
        ur.delete(id);
        //then
        assertEquals(0, ur.findAll().size(), "Kontrollera så att INGA rader existerar efter delete");
        assertTrue(ur.findById(id).isEmpty(), "Kontrollera så att ID efter delete returnerar tom Optional");
    }
    @Test
    @DisplayName("DuplicateUsernameException ska kastas om användarnamn redan finns i databasen")
    void testDuplicateUsernameException(){
        //given
        Users user = ur.create(new Users("Tobias007", "Checka för custom e", LocalDateTime.now()));
        //when//then
        assertThrows(DuplicateUsernameException.class, () -> ur.create(new Users("Tobias007", "Denna ska kasta", LocalDateTime.now())));
    }
}

