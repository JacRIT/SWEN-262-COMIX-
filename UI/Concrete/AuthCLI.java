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
        "\t--searchBy=<value> - search comics by\n\t\t\"partial-search\", \"exact-search\", \"exact-number\", \"value\", \"run-search\", \"gap-search\"");

    super.log("AP <copy id> - Add a comic into your personal collection");

    super.log("BP - Browse all the comics in your personal collection");

    super.log("Import <FilePath> - Import the specified file with comic data into the database", false);
    super.log("\tFile Path must end in either .json, .xml, or .csv", false);
    super.log("\t--p - Add imported comics directly to your personal collection");

    super.log("Export <FileName> - Export the database into a file with the specified fileName", false);
    super.log("\tFile Name must end in either .json, .xml, or .csv");


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
