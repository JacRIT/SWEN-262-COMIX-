package UI;

import java.util.Scanner;

import Api.ComixAPIFacade;

public class CLI {

  private String previousInput;
  private ComixAPIFacade api;

  public CLI() {
    this.api = new ComixAPIFacade();
  }

  /**
   * Command Line interface will be ran from this function
   */
  public static void main(String[] args) {
    System.out.println("Welcome to the Comix Application");
    System.out.println("Press \"I\" for instructions");
    System.out.println();

    Scanner scanner = new Scanner(System.in);
    CLI cli = new CLI();

    // System.out.print("-> ");
    while (true) {
      try {
        System.out.print("-> ");
        String input = scanner.nextLine();

        Boolean exit = cli.reader(input.trim());

        if (exit)
          break;

      } catch (Error err) {
        System.out.println("Something went wrong");
        break;
      }
    }

    scanner.close();
  }

  /**
   * Simple logging function for nice printing to the command line
   * 
   * @param text  : String to log into command line
   * @param space : Boolean to include a space after or not
   */
  private void log(String text, Boolean space) {
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
  private void log(String text) {
    this.log(text, true);
  }

  /**
   * Take the input from the user and map the command to the correct functionality
   * 
   * @param input : String entered by the user
   */
  public Boolean reader(String input) {

    this.log("Input:", false);
    this.log(input);

    if (input.equals("I") || input.equals("i")) {
      this.previousInput = null;
      this.instructions();
      return false;
    }

    if (input.equals("L") || input.equals("l")) {
      this.previousInput = input;
      this.loginInstructions();
      return false;
    }

    if (input.equals("Exit")) {
      this.previousInput = null;
      return true;
    }

    if (previousInput.equals("L") || previousInput.equals("l")) {
      this.login(input);
      return false;
    }

    this.log("Command (" + input + ") not found");

    return false;
  }

  /**
   * Print the instructions for how to use the application
   */
  private void instructions() {
    this.log("Welcome to the Comix Application");
    this.log("I - Instructions to use the comix application");
    this.log("L - Log into your account");
    this.log("Exit - Exit Comix");

    return;
  }

  /**
   * Print the instructions for a user to log into the system
   */
  private void loginInstructions() {
    this.log("Please enter your username:");
    // this.log("Username");
  }

  /**
   * Send the users username to the facade
   * and print welcome message if a user is returned from the facade
   * TODO: take away id from authenticate paramater
   */
  private void login(String userName) {
    this.api.authenticate(userName, 0);
    this.log("Welcome, " + userName);
    this.previousInput = null;
  }

}