import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuOptions {
  public static void listAllItems(Cafe esql) throws SQLException {
    String query = "select itemName, type, price, imageURL, description from Menu";
    int numMenuItems = esql.executeQueryAndPrintResult(query);

    if (numMenuItems == 0) {
      System.out.println("No menu items found.");
    }
    else {
      System.out.println(numMenuItems + " menu item(s) found. \n");
    }
  }

  public static void searchItemByName(Cafe esql, String name) throws SQLException {
    String query = "SELECT itemName, price, description FROM Menu WHERE itemName = '7up'";
    int numMenuItems = esql.executeQueryAndPrintResult(query);
    /*
    PreparedStatement stmt = esql.getConnection().prepareStatement(query);
    stmt.setString(1, name);

    ArrayList<List<String>> result = new ArrayList<List<String>>();
    ResultSet rows = stmt.executeQuery();

    while (rows.next()) {
      List<String> row = new ArrayList<String>();
      row.add(rows.getString("itemName").replaceAll(" ", ""));
      row.add(rows.getString("price").replaceAll(" ", ""));
      row.add(rows.getString("description").replaceAll(" ", ""));
      result.add(row);
    }

    if (result.size() == 0) {
      System.out.println("No menu items found.");
    }
    else {
      String[][] output = result.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);

      PrettyPrinter prettyPrinter = new PrettyPrinter(System.out);
      prettyPrinter.print((String[][]) output);
      System.out.println(result.size() + " menu item(s) found. \n");

    }
     */
  }

  public static void searchItemsByType(Cafe esql, String type) throws SQLException {
    String query = String.format("SELECT itemName, price, description FROM Menu WHERE type = '%s'", type);
    int searchResultCount = esql.executeQuery(query);

    if (searchResultCount == 0) {
      PrettyPrinter.printMessage("No item found with the given name.");
    } else {
      esql.executeQueryAndPrintResult(query);
    }
  }

  public static void updateItem() {}

  public static void deleteItem(int id) {}
}
