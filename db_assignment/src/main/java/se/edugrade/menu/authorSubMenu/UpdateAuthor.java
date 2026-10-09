package se.edugrade.menu.authorSubMenu;

import se.edugrade.dao.AuthorDAO;
import se.edugrade.model.Author;
import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class UpdateAuthor {

    // Uppdaterar en author om rätt ID anges

    public static void run() {
        AuthorDAO dao = new AuthorDAO();
        Scanner scanner = new Scanner(System.in);
        boolean stayInMenu = true;

        while (stayInMenu) {
            System.out.println(Helper.colorS("cyan", "\n=== ✏️ Update Author ==="));
            List<Author> authors = dao.getAllAuthors();
            if (authors.isEmpty()) {
                System.out.println(Helper.colorS("red", "⚠️ No authors found."));
                return;
            }

            authors.forEach(System.out::println);

            System.out.print(Helper.colorS("green", "\nEnter ID of author to update: "));
            int id = UserInput.getInt();
            Author author = dao.getAuthorById(id);

            if (author == null) {
                System.out.println(Helper.colorS("red", "⚠️ No author found with ID: " + id));
            } else {
                System.out.println(Helper.colorS("yellow", "\nCurrent author info:\n") + author);

                System.out.print(Helper.colorS("green", "New name (leave blank to keep): "));
                String name = scanner.nextLine().trim();
                if (!name.isEmpty()) author.setName(name);
                
                // Slänger in användaren i en loop vid fel input, kräver input i rätt format
                while (true) {

                    System.out.print(Helper.colorS("green", "New birthdate (YYYY-MM-DD, blank to keep): "));
                    String birthInput = scanner.nextLine().trim();
                    if (birthInput.isEmpty()) break;
                    {
                        try {
                            author.setBirthDate(Date.valueOf(birthInput));
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(Helper.colorS("red",
                                    "⚠️ Invalid date format. Please use correct format YYYY-MM-DD."));
                        }
                    }
                }
                System.out.print(Helper.colorS("green", "New nationality (leave blank to keep): "));
                String nationality = scanner.nextLine().trim();
                if (!nationality.isEmpty()) author.setNationality(nationality);

                dao.updateAuthor(author);
                System.out.println(Helper.colorS("yellow", "\n✅ Author updated:\n") + author);


                stayInMenu = Helper.subMenuExit();
            }
        }
    }
}
