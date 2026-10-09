package se.edugrade.menu;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.entities.Users;
import se.edugrade.utility.UserInput;

public class MainMenu {
    public static void run(EntityManagerFactory emf, Users currentUser){
        boolean loop = true;
        while (loop  == true){

            System.out.println("------------------------");
            System.out.println("MAIN MENU");
            System.out.println("------------------------");
            System.out.println("1. 📨Post options");
            System.out.println("2. 👤Users options");
            System.out.println("3. 📊Analytics");
            System.out.println("4. 💻System");
            System.out.println("5. 💬Comment options: ");
            System.out.println("0. 🔚Exit program");

            switch (UserInput.getInt()) {
                case 1 -> PostMenu.run(emf, currentUser);
                case 2 -> UserMenu.run(emf, currentUser);
                case 3 -> AnalyticsMenu.run(emf, currentUser);
                case 4 -> SystemMenu.run(emf, currentUser);
                case 5 -> CommentMenu.run(emf, currentUser);
                case 0 -> loop = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
