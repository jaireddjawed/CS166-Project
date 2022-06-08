import java.sql.SQLException;

public class ProfileOptions {
  public static void listUsers(Cafe esql) throws SQLException {
    String currentUserId = esql.getUser().getLogin();
    String query = String.format("SELECT * FROM Users WHERE login != '%s'", currentUserId);
    int numUsers = esql.executeQuery(query);

    if (numUsers == 0) {
      PrettyPrinter.printMessage("No users found.");
    } else {
      esql.executeQueryAndPrintResult(query);
      System.out.println(numUsers + " user(s) found. \n");
    }
  }

  public static void updateUser(Cafe esql, String userLogin, String phoneNum, String favItems, String type, String password) throws SQLException {
    String query = String.format("UPDATE Users SET phoneNum = '%s', favItems = '%s', type = '%s', password = '%s' WHERE login = '%s'", phoneNum, favItems, type, password, userLogin);
    try {
      esql.executeUpdate(query);
      PrettyPrinter.printMessage("Profile updated successfully.");
    } catch (SQLException e) {
      System.out.println("Error updating profile.");
    }
  }

  public static void updateProfile(Cafe esql, String userLogin, String phoneNum, String favItems, String password) throws SQLException {
    String query = String.format("UPDATE Users SET phoneNum = '%s', favItems = '%s', password = '%s' WHERE login = '%s'", phoneNum, favItems, password, userLogin);
    try {
      esql.executeUpdate(query);
      PrettyPrinter.printMessage("Profile updated successfully.");
    } catch (SQLException e) {
      System.out.println("Error updating profile.");
    }
  }
}
