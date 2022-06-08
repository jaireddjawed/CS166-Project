import java.sql.SQLException;

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
    String query = String.format("select itemName, type, price, imageURL, description from Menu where itemName = '%s'", name);
    int numMenuItems = esql.executeQueryAndPrintResult(query);

    if (numMenuItems == 0) {
      System.out.println("No menu items found.");
    }
    else {
      System.out.println(numMenuItems + " menu item(s) found. \n");
    }
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

  public static void addItem(Cafe esql, String name, String type, String price, String description, String imageURL) throws SQLException {
    String query = String.format("INSERT INTO Menu (itemName, type, price, imageURL, description) VALUES ('%s', '%s', '%s', '%s', '%s')", name, type, price, imageURL, description);
    esql.executeUpdate(query);
    PrettyPrinter.printMessage("Item added successfully.");
  }

  public static void updateItem(Cafe esql, String name, String type, String price, String description, String imageURL) throws SQLException {
    String query = String.format("UPDATE Menu SET type = '%s', price = '%s', imageURL = '%s', description = '%s' WHERE itemName = '%s'", type, price, imageURL, description, name);
    esql.executeUpdate(query);
    PrettyPrinter.printMessage("Item updated successfully.");
  }

  public static void deleteItem(Cafe esql, String name) throws SQLException {
    String query = String.format("DELETE FROM Menu WHERE itemName = '%s'", name);
    esql.executeUpdate(query);
    PrettyPrinter.printMessage("Item deleted successfully.");
  }
}
