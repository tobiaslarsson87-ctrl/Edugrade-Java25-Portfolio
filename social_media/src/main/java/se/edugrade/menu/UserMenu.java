package se.edugrade.menu;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.UsersDTO;
import se.edugrade.dto.subclasses.ContentType;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.NoActiveUser;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.UsersService;
import se.edugrade.utility.Colors;
import se.edugrade.utility.UserInput;
import java.util.List;
import java.util.Optional;

public class UserMenu {
    public static void run(EntityManagerFactory emf, Users currentUser){
        boolean loop = true;
        while(loop){
            System.out.println("---- USER MENU ----");
            System.out.println("[1] 🫂Follow user");
            System.out.println("[2] ➕Create User");
            System.out.println("[3] ♻️Update Bio");
            System.out.println("[4] ❌Delete User");
            System.out.println("[5] 👤Show profiles");
            System.out.println("[6] 📝Show Your Posts");
            System.out.println("[7] 👥Show Friends Posts");
            System.out.println("[0] 🔙Back to main menu");

            switch(UserInput.getInt()){
                case 1 -> follow(emf, currentUser);
                case 2 -> createNewUser(emf);
                case 3 -> updateBio(emf, currentUser);
                case 4 -> deleteUser(emf, currentUser);
                case 5 -> showProfile(emf, currentUser);
                case 6 -> showYourPosts(emf, currentUser);
                case 7 -> showFriendFeed(emf, currentUser);
                case 0 -> loop =false;
            }
        }
    }

    //FÖLJ ANDRA ANVÄNDARE MED NUVARANDE VALDA ANVÄNDARE
    private static void follow(EntityManagerFactory emf, Users currentUser){
        final String R = Colors.rgb(200, 25, 25);
        final String Y = Colors.rgb(200, 200, 25);
        final String X = Colors.reset();
        UsersRepository ur = new UsersRepository(emf);
        UsersService us = new UsersService(ur);
        List<UsersDTO> users = us.showAll();
        for (UsersDTO user: users){
            System.out.println(user);
        }

        System.out.println(Y+"<Chose someone to follow>"+X);

        Optional<Users> followThis = ur.findById(UserInput.getLong());
        if(followThis.isPresent()){

            //Förhindra att man följer sig själv
            if(followThis.get().getId() == currentUser.getId()){
                System.out.println(R+"<You can't follow yourself>"+X);
                return;
            }
            //Förhindra att man kan föja någon man redan följer, Set garderar redan mot dubletter men för UI-Feedback
            if(currentUser.getFollowing().contains(followThis.get())){
                System.out.println(R+currentUser.getUserName() + " is already following " + followThis.get().getUserName()+X);
            } else {
                Users user = followThis.get();
                us.followService(currentUser.getId(), user.getId());
                currentUser = ur.findById(currentUser.getId()).get(); //hämta instans med uppdaterad data
                user = ur.findById(user.getId()).get(); //hämta instans med uppdaterad data
                System.out.println(currentUser + " followed " + user);
            }
        }
    }

    //SKAPA EN NY ANVÄNDARE
    public static void createNewUser(EntityManagerFactory emf){
        UsersRepository ur = new UsersRepository(emf);
        final String G = Colors.rgb(25,200,25);
        final String R = Colors.rgb(200,25,25);
        final String Y = Colors.rgb(200,200,25);
        final String X = Colors.reset();
        boolean loop = true;
        while(loop){
            System.out.println(Y+"<Type a Username>"+X);
            String username = UserInput.getString();
            System.out.println(Y+"<Write a Biography>"+X);
            String bio = UserInput.getString();
            boolean duplicate = ur.findAll().stream()
                    .anyMatch(u -> u.getUserName().equals(username));
            if(duplicate){
                System.out.println(R+"<Existing Username>"+X);
                System.out.println(Y+"<Try a different Username>"+X);
            } else {
                System.out.println(Y+"❓Save new user: " + " 👤" + username + " 📋" + bio );
                System.out.println(G+"[1] 💾Save"+X);
                System.out.println(R+"[2] ❌Skip "+X);
                switch(UserInput.getInt()){
                    case 1 -> {
                        ur.create(new Users(username, bio));
                        loop = false;
                    }
                    case 2 -> loop = false;
                    default -> System.out.println(R+"<Invalid choice>"+X);
                }
            }
        }
    }

    //ÄNDRA BIOGRAFI PÅ CURRENT USER
    private static void updateBio(EntityManagerFactory emf, Users currentUser){
        final String G = Colors.rgb(25,200,25);
        final String Y = Colors.rgb(200,200,25);
        final String X = Colors.reset();
        UsersRepository ur = new UsersRepository(emf);

        System.out.println(Y+"<Write a new biography for your user profile>"+X);
        String newBio = UserInput.getString();
        currentUser.setBio(newBio);
        Users managed = ur.update(currentUser);
        System.out.println(G+"<New User Profile>"+X);
        System.out.println(managed);
    }
    //Ta BORT CURRENT USER OCH LOGGA UT
    private static void deleteUser(EntityManagerFactory emf, Users currentUser){
        final String G = Colors.rgb(25,200,25);
        final String R = Colors.rgb(200,25,25);
        final String Y = Colors.rgb(200,200,25);
        final String X = Colors.reset();
        UsersRepository ur = new UsersRepository(emf);

        System.out.println(Y+"<ARE YOU SURE YOU WANT TO DELETE THIS USER PROFILE. YOU WILL BE LOGGED OUT>"+X);
        System.out.println(G+"[1] YES"+X);
        System.out.println(R+"[2] NO"+X);

        switch (UserInput.getInt()){
            case 1 -> {
                ur.delete(currentUser.getId());
                throw new NoActiveUser(R+"<Logging out because of no active user>"+X);
            }
            default -> System.out.println(G+"[NO PROFILE DELETED]"+X);
        }
    }

    //VISA ALLA ANVÄNDARE
    private static void showProfile(EntityManagerFactory emf, Users currentUser){
        UsersRepository ur = new UsersRepository(emf);
        UsersService us = new UsersService(ur);
        List<UsersDTO> users = us.showAll();
        for (UsersDTO user: users){
            System.out.println(user);
        }

    }
    //VISA POSTS SKRIVNA AV ANVÄNDARE DU FÖLJER
    private static void showFriendFeed(EntityManagerFactory emf, Users currentUser){
        UsersRepository ur = new UsersRepository(emf);
        UsersService us = new UsersService(ur);
        List<ContentType> friendFeed = us.friendPosts(currentUser);
        System.out.println("\n"); //enkelt radbyte innan feed
        for(ContentType p : friendFeed){
            System.out.println(p);
            System.out.println("-".repeat(10));
        }
    }

    //VISA POSTS SKRIVNA AV DIG
    private static void showYourPosts(EntityManagerFactory emf, Users currentUser) {
        UsersRepository ur = new UsersRepository(emf);
        UsersService us = new UsersService(ur);
        List<ContentType> yourPosts = us.browsingPosts(currentUser);
        System.out.println("\n"); //linebreak innan feed
        for(ContentType p : yourPosts){
            System.out.println(p);
            System.out.println("-".repeat(10));
        }
    }
}
