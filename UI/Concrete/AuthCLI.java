package UI.Concrete;

import Model.JavaObjects.User;

public class AuthCLI extends DefaultCLI {

  private User user;

  public AuthCLI(User user) {
    this.user = user;
  }

  @Override
  public void instructions(Boolean includeLast) {
    super.instructions(false);

    super.log("SP <Search Term> - Search through all of your personal collections Comics", false);
    super.log("\tOptional Flags:", false);
    super.log("\t[--sortBy=<value>] - sort results by\n\t\t\"title\", \"publication\", \"issue\", \"volume\"", false);
    super.log(
        "\t[--searchBy=<value>] - search comics by\n\t\t\"partial-search\", \"exact-search\", \"exact-number\", \"value\"");

    this.log("L - Logout of your account");

    if (includeLast)
      super.log("Exit - Exit Comix");
  }

}
