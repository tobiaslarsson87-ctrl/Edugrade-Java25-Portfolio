package se.edugrade.menu.authorSubMenu;

import se.edugrade.dao.AuthorDAO;
import se.edugrade.model.Author;
import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.sql.Date;
import java.util.List;

/**
 * Låter användaren se alla authors samt lägga till en ny användare.
 * Skriver sedan ut alla authors på nytt.
 */
public class AddAndShowAuthors {

    /* När jag behöver accessa authorDAO från flera metoder måste den ligga utanför
    metoderna som ett private static field -
*/
    private static AuthorDAO authorDAO = new AuthorDAO();

    public static Author call() {


        System.out.println(Helper.colorS("green", "Add new author - "));
        String name = UserInput.getString();

        Date birthDate = null;
        while (birthDate == null) {
            System.out.print(Helper.colorS("green", "Birthdate (YYYY-MM-DD): "));
            String birth = UserInput.getString();
            try {
                birthDate = Date.valueOf(birth);
            } catch (IllegalArgumentException e) {
                System.out.println(Helper.colorS("red", "⚠️ Wrong input! Please use: YYYY-MM-DD."));
            }
        }

        System.out.print(Helper.colorS("green", "Nationality: "));
        String nationality = UserInput.getString();

        Author newAuthor = new Author(name, birthDate, nationality);
        int id = authorDAO.insertAuthor(newAuthor);
        newAuthor.setId(id);
        System.out.println(Helper.colorS("yellow", "\n📌 New author added!: "));
        System.out.println(newAuthor);
        showAuthors();
        return newAuthor;

    }
    // La till ett eget alternativ för showauthors

    public static void showAuthors() {
        System.out.println(Helper.colorS("blue", "\n📖 All authors in the database : "));
        List<Author> authors = authorDAO.getAllAuthors();
        for (Author a : authors) {
            System.out.println(a);
        }
    }
}
