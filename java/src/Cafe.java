/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Cafe {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                 new InputStreamReader(System.in));

   private User user = null;

   // user setters and getters
   public void setUser(User user) {
      this.user = user;
   }

   public User getUser() {
      return user;
   }

   public Connection getConnection() {
      return _connection;
   }

   /**
    * Creates a new instance of Cafe
      *
      * @param hostname the MySQL or PostgreSQL server hostname
      * @param database the name of the database
      * @param username the user name used to login to the database
      * @param password the user login password
      * @throws java.sql.SQLException when failed to make a connection.
      */
   public Cafe(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Cafe

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
      * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
      *
      * @param sql the input SQL string
      * @throws java.sql.SQLException when update failed
      */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
      * method issues the query to the DBMS and outputs the results to
      * standard out.
      *
      * @param query the input query string
      * @return the number of rows returned
      * @throws java.sql.SQLException when failed to execute the query
      */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      

      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
         ** obtains the metadata object for the returned result set.  The metadata
         ** contains row and column info.
         */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;

      // convert the database information into an array list so that it can be pretty-printted
      ArrayList<List<String>> result = new ArrayList <List<String>>();

      result.add(new ArrayList<String>());

      while (rs.next()){
         if (outputHeader) {
            for (int i = 1; i <= numCol; i++) {
               String columnName = rsmd.getColumnName (i);
               result.get(rowCount).add(columnName);
            }

            outputHeader = false;
            rowCount++;
         }

         result.add(new ArrayList<String>());

         for (int i=1; i<=numCol; i++) {
            String columnValue = rs.getString (i);
            result.get(rowCount).add(columnValue.replaceAll(" ", ""));
         }

         rowCount++;
      }//end while

      String[][] output = result.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);

      PrettyPrinter printer = new PrettyPrinter(System.out);
      printer.print(output);

      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
      * method issues the query to the DBMS and returns the results as
      * a list of records. Each record in turn is a list of attribute values
      *
      * @param query the input query string
      * @return the query result as a list of records
      * @throws java.sql.SQLException when failed to execute the query
      */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
         ** obtains the metadata object for the returned result set.  The metadata
         ** contains row and column info.
         */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();

      // iterates through the result set and saves the data returned by the query.
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
         List<String> record = new ArrayList<String>();
      for (int i=1; i<=numCol; ++i)
         record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
      * method issues the query to the DBMS and returns the number of results
      *
      * @param query the input query string
      * @return the number of rows returned
      * @throws java.sql.SQLException when failed to execute the query
      */
   public int executeQuery (String query) throws SQLException {
         // creates a statement object
         Statement stmt = this._connection.createStatement ();

         // issues the query instruction
         ResultSet rs = stmt.executeQuery (query);

         int rowCount = 0;

         // iterates through the result set and count nuber of results.
         while (rs.next()){
            rowCount++;
         }//end while
         stmt.close ();
         return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
      * method issues the query to the DBMS and returns the current
      * value of sequence used for autogenerated keys
      *
      * @param sequence name of the DB sequence
      * @return current value of a sequence
      * @throws java.sql.SQLException when failed to execute the query
      */
   public int getCurrSeqVal(String sequence) throws SQLException {
   Statement stmt = this._connection.createStatement ();

   ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
   if (rs.next())
      return rs.getInt(1);
   return -1;
   }

   /**
    * Method to close the physical connection if it is open.
      */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
      *
      * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
      */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Cafe.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Cafe esql = null;
      try{
         // use postgres JDBC driver.
         // todo: uncomment this portion when done
         // Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Cafe object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Cafe (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");

            User authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2:
                  esql.setUser(LogIn(esql));
                  authorisedUser = esql.getUser();
                  break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
               boolean usermenu = true;
               while(usermenu) {
                  System.out.println("MAIN MENU");
                  System.out.println("---------");
                  System.out.println("1. Goto Menu");
                  System.out.println("2. Update Profile");
                  System.out.println("3. Place a Order");
                  System.out.println("4. Update a Order");
                  System.out.println("5. View Order History");
                  System.out.println(".........................");
                  System.out.println("9. Log out");
                  switch (readChoice()){
                     case 1: Menu(esql); break;
                     case 2: UpdateProfile(esql); break;
                     case 3: PlaceOrder(esql); break;
                     case 4: UpdateOrder(esql); break;
                     case 5: ViewOrderHistory(esql); break;
                     case 9:
                        usermenu = false;
                        authorisedUser = null;
                     break;
                     default : System.out.println("Unrecognized choice!"); break;
                  }
               }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
      * Reads the users choice given from the keyboard
      * @int
      **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
      * Creates a new user with privided login, passowrd and phoneNum
      **/
   public static void CreateUser(Cafe esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user phone: ");
         String phone = in.readLine();
      

      // users are customers by default
         String type="Customer";
         String favItems="";

               String query = String.format("INSERT INTO USERS (phoneNum, login, password, favItems, type) VALUES ('%s','%s','%s','%s','%s')", phone, login, password, favItems, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
      * Check log in credentials for an existing user
      * @return User login or null is the user does not exist
      **/
   public static User LogIn(Cafe esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE login = '%s' AND password = '%s' LIMIT 1", login, password);
         List<List<String>> userResult = esql.executeQueryAndReturnResult(query);
         int userNum = userResult.size();

         if (userNum == 1) {
            String loginRes = userResult.get(0).get(0);
            String typeRes = userResult.get(0).get(4);
            User user = new User(loginRes, typeRes);
            return user;
         }
         else {
               System.out.println("Invalid login or password!");
               return null;
         }
      } catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   // Rest of the functions definition go in here

   public static void Menu(Cafe esql) {
      System.out.print("\n");
      System.out.println("CAFE MENU");

      System.out.println("---------");
      System.out.println("1. View all items");
      System.out.println("2. Search for an item by name");
      System.out.println("3. Search for an item by type");

      // only print items 4-6 if the user is a manager
      if (esql.getUser().getType().equals("Manager")) {
         System.out.println("4. Add an item");
         System.out.println("5. Update an item");
         System.out.println("6. Delete an item");
      }

      System.out.println("---------");

      switch (readChoice()){
         case 1:
               try {
                  MenuOptions.listAllItems(esql);
               } catch (SQLException e) {
                  System.out.println("Failed to list all items");
               } break;
         case 2:
            System.out.print("\tEnter item name: ");

            try {
               String itemName = in.readLine();
               MenuOptions.searchItemByName(esql, itemName);
            }
            catch (SQLException e) {
               System.out.println("There was an error with your query.");
            }
            catch (IOException e) {
               System.out.println("There was an error with your input.");
            }

            break;
         case 3:
            System.out.print("\tEnter item type: ");

            try {
               String itemType = in.readLine();
               MenuOptions.searchItemsByType(esql, itemType);
            }
            catch (SQLException e) {
               System.out.println("There was an error listing all items.");
            }
            catch (IOException e) {
               System.out.println("There was an error with your input.");
            }
            break;
         case 4:
            if (esql.getUser().getType().equals("Manager")) {
               try {
                  String itemName;

                  do {
                     System.out.print("\tEnter item name: ");
                     itemName = in.readLine();
                     if (itemName.equals("")) {
                        System.out.println("Invalid item name!");
                     }
                  } while (itemName.equals(""));

                  String itemType;

                  do {
                     System.out.print("\tEnter item type: ");
                     itemType = in.readLine();
                     if (itemType.equals("")) {
                        System.out.println("Invalid item type!");
                     }
                  } while (itemType.equals(""));

                  String itemPrice;
                  do {
                     System.out.print("\tEnter item price: ");
                     itemPrice = in.readLine();
                     if (itemPrice.equals("")) {
                        System.out.println("Invalid item price!");
                     }
                  } while (itemPrice.equals(""));

                  System.out.print("\tEnter item description (optional): ");
                  String itemDescription = in.readLine();

                  System.out.print("\tEnter item URL (optional): ");
                  String itemURL = in.readLine();

                  MenuOptions.addItem(esql, itemName, itemType, itemPrice, itemDescription, itemURL);
               }
               catch (SQLException e) {
                  System.out.println("Failed to add item");
               }
               catch (IOException e) {
                  System.out.println("There was an error with your input.");
               }
            }
            else {
               System.out.println("Unrecognized choice!");
            }
            break;
         case 5:
            if (esql.getUser().getType().equals("Manager")) {
               try {
                  MenuOptions.listAllItems(esql);
               }
               catch (SQLException e) {
                  System.out.println("Failed to list all items");
               }

               try {
                  System.out.print("\tEnter item name: ");
                  String itemName = in.readLine();

                  String itemType;

                  do {
                     System.out.print("\tEnter item type: ");
                     itemType = in.readLine();
                     if (itemType.equals("")) {
                        System.out.println("Invalid item type!");
                     }
                  } while (itemType.equals(""));

                  String itemPrice;
                  do {
                     System.out.print("\tEnter updated item price: ");
                     itemPrice = in.readLine();
                     if (itemPrice.equals("")) {
                        System.out.println("Invalid item price!");
                     }
                  } while (itemPrice.equals(""));

                  System.out.print("\tEnter updated item description: ");
                  String itemDescription = in.readLine();

                  System.out.print("\tEnter updated item URL: ");
                  String itemURL = in.readLine();

                  MenuOptions.updateItem(esql, itemName, itemType, itemPrice, itemDescription, itemURL);
               }
               catch (SQLException e) {
                  System.out.println("Failed to delete item");
               }
               catch (IOException e) {
                  System.out.println("There was an error with your input.");
               }
            }
            break;
         case 6:
            if (esql.getUser().getType().equals("Manager")) {
               try {
                  MenuOptions.listAllItems(esql);
               }
               catch (SQLException e) {
                  System.out.println("Failed to list all items");
               }

               try {
                  System.out.print("\tEnter item name: ");
                  String itemName = in.readLine();

                  MenuOptions.deleteItem(esql, itemName);
               }
               catch (SQLException e) {
                  System.out.println("Failed to delete item");
               }
               catch (IOException e) {
                  System.out.println("There was an error with your input.");
               }
            }
            else {
               System.out.println("Unrecognized choice!");
            }
            break;
         default : System.out.println("Unrecognized choice!"); break;
      }//end switch
   }

   public static void UpdateProfile(Cafe esql){
      // update user's own profile information
      // if they are a manager, they can update other users' information
   }

   public static void PlaceOrder(Cafe esql){
      // users can place an order

      try {
         MenuOptions.listAllItems(esql);
      }
      catch (SQLException e) {
         System.out.println("There was an error retrieving all items.");
      }

      String moreItems = "y";
      double total = 0;

      while (moreItems.equals("y")) {
         try {
            System.out.print("Please enter the item name you would like to order: ");
            String itemName = in.readLine();

            String checkMenuItemExistsQuery = "select * from MENU where itemName = '" + itemName + "'";
            List<List<String>> checkMenuItemExistsResult = esql.executeQueryAndReturnResult(checkMenuItemExistsQuery);

            if (checkMenuItemExistsResult.size() == 0) {
               System.out.println("Item does not exist in the menu. Please try again.");
               continue;
            }
            else {
               double price = Double.parseDouble(checkMenuItemExistsResult.get(0).get(2));

               System.out.print("Please enter the quantity you would like to order: ");
               int quantity = Integer.parseInt(in.readLine());

               total += price * quantity;

               System.out.print("Do you want to order more items? (y/n): ");
               moreItems = in.readLine();
            }
         }
         catch (IOException e) {
            System.out.println("There was an error reading your input.");
         }
         catch (SQLException e) {
            System.out.println("There was an error checking your item.");
         }
      }

      String insertOrderQuery = "insert into Orders (orderid, login, paid, timeStampRecieved, total) values (default, '" + esql.getUser().getLogin() + "', false, NOW(), " + total + ")";

      try {
         esql.executeUpdate(insertOrderQuery);

         System.out.println("\n");
         System.out.println("+----------------------------+");
         System.out.println("|                            |");
         System.out.println("|  Thank you for your order. |");
         System.out.println("|        Total: $" + total + "         |");
         System.out.println("|                            |");
         System.out.println("+----------------------------+");
         System.out.println("\n");
      }
      catch (SQLException e) {
         System.out.println("There was an error inserting your order.");
      }
   }

   public static void UpdateOrder(Cafe esql){
      // customers can update any non-paid orders
      System.out.print("\tEnter order ID: ");
      try {
         String orderId = in.readLine();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public static void ViewOrderHistory(Cafe esql){
      // view all orders for a user
   }

   public static void ChangeOrderToPaid(Cafe esql) {
      // employees and managers can change any order to paid
   }
}//end Cafe

