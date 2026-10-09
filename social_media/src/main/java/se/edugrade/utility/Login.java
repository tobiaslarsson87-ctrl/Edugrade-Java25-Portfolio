package se.edugrade.utility;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.SimpleUsersDTO;
import se.edugrade.entities.Users;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.UsersService;
import java.util.List;
import java.util.Optional;

public class Login {
    EntityManagerFactory emf;

    public Login(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Users selectUser(){
        UsersRepository ur = new UsersRepository(emf);
        UsersService us = new UsersService(ur);
        while(true){
            System.out.println("[SELECT A USER TO LOGIN AS]");
            System.out.println("-".repeat(15));

            List<SimpleUsersDTO> selection = us.listAll();
            for (SimpleUsersDTO u : selection){
                System.out.println(u);
            }

            Optional<Users> activeUser = ur.findById(UserInput.getLong());
            if(activeUser.isEmpty()){
                System.out.println("No User picked");
            }
            else {
                return activeUser.get();
            }
        }
    }
}
