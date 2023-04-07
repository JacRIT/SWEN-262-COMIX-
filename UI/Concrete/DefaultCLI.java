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

    this.log("S <Search Term> - Search through all Comics", false);
    this.log("\tOptional Flags:", false);
    this.log("\t[--sortBy=<value>] - sort results by\n\t\t\"title\", \"publication\", \"issue\", \"volume\"", false);
    this.log(
        "\t[--searchBy=<value>] - search comics by\n\t\t\"partial-search\", \"exact-search\", \"exact-number\", \"value\"");

    if (includeLast)
      this.log("Exit - Exit Comix");

  }

  @Override
  public String readInput(Scanner scanner) {

    System.out.print("-> ");
    if (scanner.hasNextLine())
      return scanner.nextLine();

    return "";
    // try {
    // Scanner scanner = new Scanner(System.in);
    // String input = null;

    // System.out.println("");
    // System.out.print("-> ");

    // while (input == null) {
    // this.log(scanner.hasNextLine() + "", false);

    // input = scanner.nextLine();

    // this.log(input + "", false);
    // this.log(input.length() + "", false);
    // }

    // // if (scanner.hasNextLine())
    // // input = scanner.nextLine();

    // scanner.close();
    // return input;
    // } catch (Exception err) {
    // this.log("", false);
    // this.log("Scanner Error:", false);
    // this.log(err.getMessage(), false);
    // this.log(err.getLocalizedMessage(), false);
    // this.log(err.toString(), false);
    // return this.readInput();
    // }
  }

  @Override
  public void displayMessage(String message) {
    this.log("", false);
    this.log(message);

  }

}
