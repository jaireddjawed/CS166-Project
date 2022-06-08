public class User {
  String login;
  String phoneNum;
  String favItems;
  String type;

  // will add the other fields later
  public User(String login, String type) {
    this.login = login.replaceAll(" ", "");
    this.type = type.replaceAll(" ", "");
  }

  public String getLogin() {
    return login;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public String getType() {
    return type;
  }
}
