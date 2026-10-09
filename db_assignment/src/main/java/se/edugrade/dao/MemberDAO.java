package se.edugrade.dao;
import se.edugrade.model.Member;
import se.edugrade.utility.DatabaseConnection;
import se.edugrade.utility.ExceptionLogger;
import se.edugrade.utility.Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    //Metod för SHOW

    public List<Member> getAllMembers () {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try ( Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) {
                        Member m = new Member(rs.getInt("id"), rs.getString("name"));
                        members.add(m);
                    }
                }
        catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem with fetching members from database");
        }
        return members;
    }

    //Metod för CREATE

    public void insertMember(Member m) {
        String sql = "INSERT INTO members (name) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
        {
            stmt.setString(1, m.getName());
            stmt.executeUpdate();
            //Insert i Databas gjord men behöver hämta det autogenererade ID från databasen.
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
            }
            //LOGGA: RETURN_GENERATED_KEYS. Checkad och fungerar ✅
            System.out.println(Helper.colorS("green", "\n\uD83D\uDC64 Added new member: " + "ID: " + m.getId() + " Name: " + m.getName()));
        }

        catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem  with inserting member");
        }
    }

    //Metod för DELETE

    public void deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("\n❌ ERROR" );
            } else {
                System.out.println(Helper.colorS("red", "\n✅ Member Removed: " + "🆔 At ID: " + id));
            }

        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem  with deleting member. Have you input an existing ID?");
        }
    }
    //Metod för UPDATE

    public void updateMember(Member m) {
        String sql = "UPDATE members SET name = ? WHERE id = ?";;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getName());
            stmt.setInt(2, m.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("\n❌ ERROR" );
            }
            else {
                System.out.println(Helper.colorS("green", "\n✅ Member information updated"));
                System.out.println(Helper.colorS("reset", "🆔 ID: " + m.getId()));
                System.out.println(Helper.colorS("yellow", "\uD83E\uDDD1 Name: " + m.getName()));
            }

        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem  with updating member. Have you input an existing ID?");
        }
    }
}
