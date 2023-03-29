package UI;

import java.util.Scanner;

import Api.ComixAPIFacade;
import Model.JavaObjects.User;

public class CLI {

  private String previousInput;
  private ComixAPIFacade api;
  private User currentUser;

  public CLI() {
    this.api = new ComixAPIFacade();
    this.currentUser = null;
    this.previousInput = "";
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

    // login a user with provided username
    if (previousInput.equals("L") || previousInput.equals("l")) {
      this.login(input);
      return false;
    }

    // register a user with provided username
    if (previousInput.equals("R") || previousInput.equals("r")) {
      this.register(input);
      return false;
    }

    // print instructions
    if (input.equals("I") || input.equals("i")) {
      this.previousInput = "";
      this.instructions();
      return false;
    }

    // start accepting a user name to register user
    if (input.equals("R") || input.equals("r")) {
      // user already logged in cannot register inside account
      if (this.currentUser != null) {
        this.log("Already Logged in " + this.currentUser.getName(), false);
        this.log("Type: \"logout\" to logout");
        return false;
      }

      this.previousInput = input;
      this.registerInstructions();
      return false;
    }

    // start accepting a user name to login a user
    if (input.equals("L") || input.equals("l")) {

      // user already logged in cannot try again
      if (this.currentUser != null) {
        this.log("Already Logged in " + this.currentUser.getName(), false);
        this.log("Type: \"logout\" to logout");
        return false;
      }

      this.previousInput = input;
      this.loginInstructions();
      return false;
    }

    // logout a user
    if (input.equals("logout") || input.equals("Logout")) {
      this.previousInput = "";
      this.logout();
      return false;
    }

    // stop the application
    if (input.equals("Exit")) {
      this.previousInput = "";
      return true;
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
    this.log("R - Register a new account");
    this.log("L - Log into your account");
    this.log("Logout - Log out of your account");
    this.log("Exit - Exit Comix");

    return;
  }

  private void registerInstructions() {
    this.log("Please enter your preferred username:");
  }

  private void register(String username) {
    User existingUser = this.api.authenticate(username);
    if (existingUser == null) {
      this.currentUser = this.api.register(username);
      this.log("Welcome, " + username);
      this.previousInput = "";
    } else {
      this.log("Username already taken. Please try a different username.");
    }

  }

  /**
   * Print the instructions for a user to log into the system
   */
  private void loginInstructions() {
    this.log("Please enter your username:");
  }

  /**
   * Send the users username to the facade
   * and print welcome message if a user is returned from the facade
   */
  private void login(String userName) {
    this.currentUser = this.api.authenticate(userName);
    if(this.currentUser == null) {
      this.log("This does not seem to be a valid username.");
      this.log("Consider typeing 'R' to register a new account.");
    } else {
      this.log("Welcome, " + userName);
    }
    this.previousInput = "";
  }

  /**
   * Send a logout request to the API and clear the current user
   */
  private void logout() {
    //this.api.unAuthenticate();    no longer in ComixAPIFacade
    this.log("Goodbye " + this.currentUser.getName());
    this.currentUser = null;
  }

}