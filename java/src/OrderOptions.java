import java.sql.SQLException;

public class OrderOptions {
  public static void updateOrder(Cafe esql) {}

  public static void viewOrderHistory(Cafe esql) throws SQLException {
    String userLogin = esql.getUser().getLogin();

    // view the last 5 orders that were made by the user
    String query = String.format("SELECT * FROM Orders WHERE login = '%s' ORDER BY orderId DESC LIMIT 5", userLogin);
    int numOrders = esql.executeQuery(query);

    if (numOrders == 0) {
      PrettyPrinter.printMessage("No orders found.");
    } else {
      esql.executeQueryAndPrintResult(query);
      System.out.println(numOrders + " order(s) found. \n");
    }
  }

  public static void viewAllOrderHistory(Cafe esql) throws SQLException {
    // view all orders that were timestampRecieved was made within 24 hours and are unpaid
    String query = "SELECT * FROM Orders WHERE paid = false AND timestampRecieved >= NOW() - INTERVAL '1 day'";
    int numOrders = esql.executeQuery(query);

    if (numOrders == 0) {
      PrettyPrinter.printMessage("No orders found.");
    } else {
      esql.executeQueryAndPrintResult(query);
      System.out.println(numOrders + " order(s) found. \n");
    }
  }

  public static String viewAllUnpaidOrders(Cafe esql) throws SQLException {
    // view all unpaid orders
    String query = "SELECT * FROM Orders WHERE paid = false";
    int numOrders = esql.executeQuery(query);

    if (numOrders == 0) {
      PrettyPrinter.printMessage("No unpaid orders found.");
      return "none";
    } else {
      esql.executeQueryAndPrintResult(query);
      System.out.println(numOrders + " unpaid order(s) found. \n");
      return "some";
    }
  }
}
