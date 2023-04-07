package UI.Concrete;

public class AuthCLI extends DefaultCLI {

  // private User user;

  public AuthCLI() {
    // this.user = user;
  }

  @Override
  public void instructions(Boolean includeLast) {
    super.instructions(false);

    super.log("SP <Search Term> - Search through all of your personal collections Comics", false);
    super.log("\tOptional Flags:", false);
    super.log(
        "\t--sortBy=<value> - Sort results by:\n\t\t\"title\", \"publication\", \"issue\", \"volume\"\n\t\tDefaults to title",
        false);
    super.log(
        "\t--searchBy=<value> - Search comics by:\n\t\t\"partial-search\", \"exact-search\", \"exact-number\", \"value\"\n\t\tDefaults to partial-search");

    super.log("A <comic id> - Add a comic into your personal collection");

    this.log("L - Logout of your account");

    if (includeLast)
      super.log("Exit - Exit Comix");
  }

}
