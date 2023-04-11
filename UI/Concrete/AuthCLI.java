package UI.Concrete;

import UI.Interfaces.Mediator;

public class AuthCLI extends DefaultCLI {

  private Mediator mediator;

  public AuthCLI(Mediator mediator) {
    this.mediator = mediator;
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

    super.log("AP <copy id> - Add a comic into your personal collection");

    super.log("BP - Browse all the comics in your personal collection");

    if (this.mediator.canUndo())
      super.log("U - Undo most recent change");

    if (this.mediator.canRedo())
      super.log("R - Redo most recent undo");

    if (this.mediator.canUndo())
      super.log("UA - Undo all changes made");

    if (this.mediator.canRedo())
      super.log("RA - Redo all undone changes");

    super.log("Create - Create a new comic for the library");

    this.log("L - Logout of your account");

    if (includeLast)
      super.log("Exit - Exit Comix");
  }

}
