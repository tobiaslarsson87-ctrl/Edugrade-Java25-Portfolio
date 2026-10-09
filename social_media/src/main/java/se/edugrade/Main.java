package se.edugrade;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import se.edugrade.entities.*;
import se.edugrade.menu.MainMenu;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.utility.DataSeeder;
import se.edugrade.utility.Login;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProdPU");
        UsersRepository ur = new UsersRepository(emf);
        DataSeeder generator = new DataSeeder(emf);

        //Fyller databasen OM den är tom.
        if(ur.findAll().isEmpty()){
            generator.seedAll();
        }

        Login login = new Login(emf);
        Users currentUser = login.selectUser();
        MainMenu.run(emf, currentUser);
        emf.close();
    }
}
