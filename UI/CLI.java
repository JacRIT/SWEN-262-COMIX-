package UI;

import java.util.Arrays;
import java.util.Scanner;

import Api.ComixAPIFacade;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class CLI {

  public static enum SortingStrategy {
    Title("Title"), Issue("Issue"), Publication("Publication"), Volume("Volume");

    private String value;

    private SortingStrategy(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  private String previousInput;
  private ComixAPIFacade api;
  private User currentUser;

  public CLI() {
    this.api = new ComixAPIFacade();
    this.currentUser = null;
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

    // print instructions
    if (input.equals("I") || input.equals("i")) {
      this.previousInput = null;
      this.instructions();
      return false;
    }

    // start accepting a user name to login a user
    if (input.equals("L") || input.equals("l")) {

      // user allready logged in cannot try again
      if (this.currentUser != null) {
        this.log("Allready Logged in " + this.currentUser.getName(), false);
        this.log("Type: \"logout\" to logout");
        return false;
      }

      this.previousInput = input;
      this.loginInstructions();
      return false;
    }

    // logout a user
    if (input.equals("logout") || input.equals("Logout")) {
      this.previousInput = null;
      this.logout();
      return false;
    }

    // stop the application
    if (input.equals("Exit") || input.equals("exit")) {
      this.previousInput = null;
      return true;
    }

    // Only commands that contain flags make it to this point

    String[] flags = this.seperateFlags(input);

    // Input was empty
    if (flags.length == 0) {
      this.log("Command (" + input + ") is missing...?");
      return false;
    }

    String baseCommand = flags[0];

    // remove base command from flags
    flags = Arrays.stream(flags).filter((flag) -> {
      return !baseCommand.equals(flag);
    }).toArray(String[]::new);

    // Browse comics with possibly specified command
    if (baseCommand.equals("B") || baseCommand.equals("b")) {
      String sortFlag = SortingStrategy.Title.toString();

      if (flags.length > 1) {
        String flag = flags[1];
        // sortFlag = flag.s
      }

      this.browseComics(input);
      return false;
    }

    this.log("Command (" + input + ") not found");

    return false;
  }

  /**
   * Destructure the input to find flags marked as --<flag>=<value>
   */
  private String[] seperateFlags(String input) {
    String[] flags = input.split("--");
    flags = Arrays.stream(flags).map((flag) -> {
      return flag.trim();
    }).toArray(String[]::new);
    return flags;
  }

  /**
   * Print the instructions for how to use the application
   */
  private void instructions() {
    this.log("Welcome to the Comix Application");
    this.log("I - Instructions to use the comix application");
    this.log("L - Log into your account");
    this.log("Browse - Browse all Comics");
    this.log("\t[--sort=<value>] - sort results by\n\t\t\"title\", \"publication\", \"issue\", \"volume\"");
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
    if (this.currentUser != null) {
      this.log("Please logout first...");
      return;
    }
    this.currentUser = this.api.authenticate(userName);
    this.log("Welcome, " + userName);
    this.previousInput = null;
  }

  /**
   * Send a logout request to the API and clear the current user
   */
  private void logout() {
    if (this.currentUser == null) {
      this.log("Must be logged in to logout");
      return;
    }
    this.api.unAuthenticate();
    this.log("Goodbye " + this.currentUser.getName());
    this.currentUser = null;
  }

  /**
   * Send a request to gather all the comics in the database
   * then prints them to the command line
   */
  private void browseComics(String inputSortStrategy) {
    String sortStrategy = "partial";
    if (inputSortStrategy.length() > 0) {
      sortStrategy = inputSortStrategy;
    }
    Comic[] allComics = this.searchComics("", "null", inputSortStrategy);
    this.log(Arrays.toString(allComics));
  }

  /**
   * Send a search request to search the entire database using
   * using the search type and sort type that the user specifies
   */
  private Comic[] searchComics(String keyword, String searchType, String sortType) {

    return new Comic[1];
  }

}