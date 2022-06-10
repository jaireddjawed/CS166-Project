import java.sql.SQLException;

public class MenuOptions {
  public static void listAllItems(Cafe esql) throws SQLException {
    // retrieve all items from the menu, then list them out (if any exist)
    String query = "select itemName, type, price, imageURL, description from Menu";
    int numMenuItems = esql.executeQuery(query);

    if (numMenuItems == 0) {
      PrettyPrinter.printMessage("No menu items found.");
    }
    else {
      esql.executeQueryAndPrintResult(query);
      System.out.println(numMenuItems + " menu item(s) found. \n");
    }
  }

  public static void searchItemByName(Cafe esql, String name) throws SQLException {
    // search for an item by name, then list it out (if found)
    String query = String.format("select itemName, type, price, imageURL, description from Menu where itemName = '%s'", name);
    int numMenuItems = esql.executeQuery(query);

    if (numMenuItems == 0) {
      PrettyPrinter.printMessage("No menu items found.");
    }
    else {
      esql.executeQueryAndPrintResult(query);
      System.out.println(numMenuItems + " menu item(s) found. \n");
    }
  }

  public static void searchItemsByType(Cafe esql, String type) throws SQLException {
    // search for items by type, then list them out (if any exist)
    String query = String.format("SELECT itemName, price, description FROM Menu WHERE type = '%s'", type);
    int searchResultCount = esql.executeQuery(query);

    if (searchResultCount == 0) {
      PrettyPrinter.printMessage("No item found with the given name.");
    } else {
      esql.executeQueryAndPrintResult(query);
    }
  }

  public static void addItem(Cafe esql, String name, String type, String price, String description, String imageURL) throws SQLException {
    // add an item to the menu
    String query = String.format("INSERT INTO Menu (itemName, type, price, imageURL, description) VALUES ('%s', '%s', '%s', '%s', '%s')", name, type, price, imageURL, description);
    esql.executeUpdate(query);
    PrettyPrinter.printMessage("Item added successfully.");
  }

  public static void updateItem(Cafe esql, String name, String type, String price, String description, String imageURL) throws SQLException {
    // update an item in the menu after searching for it by name
    String query = String.format("UPDATE Menu SET type = '%s', price = '%s', imageURL = '%s', description = '%s' WHERE itemName = '%s'", type, price, imageURL, description, name);
    esql.executeUpdate(query);
    PrettyPrinter.printMessage("Item updated successfully.");
  }

  public static void deleteItem(Cafe esql, String name) throws SQLException {
    // remove the itemstatus of an item (because it depends on the item's primary key)
    String itemStatusQuery = String.format("delete from ItemStatus where itemName = '%s'", name);
    esql.executeUpdate(itemStatusQuery);

    // delete the item
    String query = String.format("DELETE FROM Menu WHERE itemName = '%s'", name);
    esql.executeUpdate(query);

    PrettyPrinter.printMessage("Item deleted successfully.");
  }
}
