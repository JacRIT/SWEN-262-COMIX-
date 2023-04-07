package UI.Concrete;

import java.util.Scanner;

import UI.Interfaces.CLI;

public abstract class DefaultCLI implements CLI {

  public DefaultCLI() {

  }

  /**
   * Simple logging function for nice printing to the command line
   * 
   * @param text  : String to log into command line
   * @param space : Boolean to include a space after or not
   */
  protected void log(String text, Boolean space) {
    System.out.println(text);
    if (space)
      System.out.println();
  }

  /**
   * Overloaded logging function for nice printing to the command line
   * always prints with a space
   * 
   * @param text : String to log into command line
   */
  protected void log(String text) {
    this.log(text, true);
  }

  @Override
  public void instructions(Boolean includeLast) {

    this.log("Welcome to the Comix Application", false);
    this.log("Enter a command below then press enter to continue to the next steps.");
    this.log("I - Instructions to use the comix application");

    this.log("V <Comic Id> - View a single comics contents", false);
    this.log("\tOptional Flags:", false);
    this.log("\t--detailed - Display the entire contents of a comic rather than a brief summary");

    this.log("S <Search Term> - Search through all Comics", false);
    this.log("\tOptional Flags:", false);
    this.log("\t--sortBy=<value> - sort results by\n\t\t\"title\", \"publication\", \"issue\", \"volume\"", false);
    this.log(
        "\t--searchBy=<value> - search comics by\n\t\t\"partial-search\", \"exact-search\", \"exact-number\", \"value\"");

    if (includeLast)
      this.log("Exit - Exit Comix");

  }

  @Override
  public String readInput(Scanner scanner) {

    System.out.print("-> ");
    if (scanner.hasNextLine())
      return scanner.nextLine();

    return "";

  }

  @Override
  public void displayMessage(String message) {
    this.log("", false);
    this.log(message);

  }

}
