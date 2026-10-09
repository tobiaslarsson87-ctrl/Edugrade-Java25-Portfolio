package se.edugrade.menu;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.UsersDTO;
import se.edugrade.entities.Users;
import se.edugrade.repositories.HashtagRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.UsersService;
import se.edugrade.utility.DatabaseBackupService;
import se.edugrade.utility.ExportJSON;
import se.edugrade.utility.Imports.ImportHashtagJSON;
import se.edugrade.utility.Imports.ImportPostJSON;
import se.edugrade.utility.Imports.ImportUserJSON;
import se.edugrade.utility.UserInput;
import java.nio.file.Path;
import java.util.List;

public class SystemMenu {
    public static void run(EntityManagerFactory emf, Users currentUser){
        boolean loop = true;
        while(loop == true){
            System.out.println("---- SYSTEM MENU ----");
            System.out.println("[1] 📤Backup database (JSON)");
            System.out.println("[2] 📤Export user data (JSON)");
            System.out.println("[3] 📥Import database backup (JSON)");
            System.out.println("[0] 🔙Back to main menu");

            switch(UserInput.getInt()){
                case 1 -> backUpDatabase(emf);
                case 2 -> exportUserMenu(emf);
                case 3 -> importUserMenu(emf);
                case 0 -> loop = false;
                default -> {
                    System.out.println("❌ Invalid choice, please choose 1 or 0.");
                    System.out.println();
                }

            }
        }
    }

    //EXPORTERA BACKUP PÅ DATABAS
    private static void backUpDatabase(EntityManagerFactory emf){

        UsersRepository ur = new UsersRepository(emf);
        PostRepository ps = new PostRepository(emf);
        HashtagRepository hs = new HashtagRepository(emf);
        DatabaseBackupService dbs = new DatabaseBackupService(ur, ps, hs);

        dbs.backupDatabase(Path.of("data", "BackUpDatabase"));
    }

    //EXPORTERA ALLA ANVÄNDARE TILL EN JSON FIL
    private static void exportUserMenu(EntityManagerFactory emf){
        UsersRepository ur = new UsersRepository(emf);
        UsersService us = new UsersService(ur);
        List<UsersDTO> users = us.showAll();

        Path filePath = Path.of("data/ExportedUsers", "users.json");
        ExportJSON.exportUserData(users, filePath);
    }

    //IMPORT DATABASE BACKUP
    private static void importUserMenu(EntityManagerFactory emf) {
        ImportUserJSON.importUserJSON(Path.of("data/BackUpDatabase", "users.json"));
        ImportPostJSON.importPostsJSON(Path.of("data/BackUpDatabase", "posts.json"));
        ImportHashtagJSON.importHashtagJSON(Path.of("data/BackUpDatabase", "hashtags.json"));
    }
}
