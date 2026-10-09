package se.edugrade.menu.authorSubMenu;

import se.edugrade.dao.AuthorDAO;
import se.edugrade.model.Author;
import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.util.List;

// Tar bort en author om IDt stämmer överens med authors som finns.
public class DeleteAuthor {

    public static void run() {
        AuthorDAO dao = new AuthorDAO();
        boolean stayInMenu = true;

        while (stayInMenu) {
            System.out.println(Helper.colorS("cyan", "\n=== 🗑️ Delete Author ==="));
            List<Author> authors = dao.getAllAuthors();
            if (authors.isEmpty()) {
                System.out.println(Helper.colorS("red", "⚠️ No authors found."));
                return;
            }

            authors.forEach(System.out::println);

            System.out.print(Helper.colorS("green", "\nEnter ID of author to delete: "));
            int id = UserInput.getInt();
            Author author = dao.getAuthorById(id);

            if (author == null) {
                System.out.println(Helper.colorS("red", "⚠️ No author found with ID: " + id));
            } else {
                System.out.println(Helper.colorS("yellow", "\nAuthor to be deleted:\n") + author);
                System.out.print(Helper.colorS("red", "Are you sure? [1=Yes, 0=No]: "));
                int confirm = UserInput.getInt();

                if (confirm == 1) {
                    dao.deleteAuthor(id);
                } else {
                    System.out.println(Helper.colorS("blue", "❎ Deletion cancelled."));
                }
            }

            stayInMenu = Helper.subMenuExit(); // användarval: stanna eller gå tillbaka
        }
    }
}
