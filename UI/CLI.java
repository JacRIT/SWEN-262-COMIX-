package UI;

import java.util.Arrays;
import java.util.Scanner;

import Api.ComixCommonAPI;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
// import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;
import Model.Search.ConcreteSearches.ExactNumberSearch;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSearches.ValueSearch;
import Model.Search.ConcreteSorts.DateSort;
// import Model.Search.SortAlgorithm;
// import Model.Search.ConcreteSearches.PartialKeywordSearch;
// import Model.Search.ConcreteSorts.DefaultSort;
import Model.Search.ConcreteSorts.DefaultSort;
import Model.Search.ConcreteSorts.IssueNumberSort;
import Model.Search.ConcreteSorts.TitleSort;
import Model.Search.ConcreteSorts.VolumeSort;

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
  private ComixCommonAPI api;
  private User currentUser;

  public CLI() throws Exception {
    this.api = new ComixCommonAPI();
    this.currentUser = null;
    this.previousInput = "";
  }

  /**
   * Command Line interface will be ran from this function
   */
  public static void main(String[] args) {
    System.out.println("Loading...");

    Scanner scanner = new Scanner(System.in);

    try {

      CLI cli = new CLI();
      System.out.println("Welcome to the Comix Application");
      System.out.println("Press \"I\" for instructions");
      System.out.println();

      // System.out.print("-> ");
      while (true) {
        try {
          System.out.print("-> ");
          String input = scanner.nextLine();

          Boolean exit = cli.reader(input.trim());

          if (exit)
            break;

        } catch (Exception err) {
          cli.log("Something went wrong", false);
          cli.log(err.getLocalizedMessage(), false);
          cli.log(err.getMessage(), false);
          cli.log(err.getCause().toString(), false);
          cli.log(err.getStackTrace().toString(), false);
          break;
        }
      }

      scanner.close();

    } catch (Exception e) {
      System.out.println("CLI Creation Exception");
    }

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
  public Boolean reader(String input) throws Exception {

    // this.log("Input:", false);
    // this.log(input);

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

    // search comics that match input
    if (previousInput.equals("Search") | previousInput.equals("search")) {
      Comic[] comics = this.searchComics(input.trim());

      this.log("", false);
      this.log("Search Results: ");
      for (Comic comic : comics) {
        this.log("\t" + comic.toString());
      }

      previousInput = "";
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
    if (input.equals("Exit") || input.equals("exit")) {
      this.previousInput = "";
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

    // intiate a search through the comics
    if (baseCommand.equals("Search") || baseCommand.equals("search")) {
      String searchCommand = "";
      String sortCommand = "";

      // grab the value from inputed flags
      for (String flag : flags) {
        String[] seperatedCommand = flag.split("=");

        // invalid syntax
        if (seperatedCommand.length != 2) {
          this.log("Flags must contain a name and value ie; \"--<name>=<value>", false);
          this.log(flag);
          return false;
        }

        String flagName = seperatedCommand[0].trim();
        String flagValue = seperatedCommand[1].trim();

        if (flagName.equals("searchBy")) {
          searchCommand = flagValue;
          continue;
        }
        if (flagName.equals("sortBy")) {
          sortCommand = flagValue;
          continue;
        }

        // invalid flag
        this.log("Unkown flag, press \"I\" for instructions");
        return false;
      }

      // validate flags are correct
      if (!this.setSearchOptions(searchCommand, sortCommand))
        return false;

      this.previousInput = baseCommand;
      this.searchInstructions();

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
    this.log("Welcome to the Comix Application", false);
    this.log("Enter a command below then press enter to continue to the next steps.");
    this.log("I - Instructions to use the comix application");
    this.log("R - Register a new account");
    if (this.currentUser == null)
      this.log("L - Log into your account");
    else
      this.log("Logout - Log out of your account");
    // this.log("Browse - Browse all Comics");
    this.log("Search - Search through all Comics", false);
    this.log("\t[--sortBy=<value>] - sort results by\n\t\t\"title\", \"publication\", \"issue\", \"volume\"", false);
    this.log(
        "\t[--searchBy=<value>] - search comics by\n\t\t\"partial-search\", \"exact-search\", \"exact-number\", \"value\"");
    this.log("Exit - Exit Comix");

    return;
  }

  private void registerInstructions() {
    this.log("Please enter your preferred username:");
  }

  /**
   * Print the instructions for a user to log into the system
   */
  private void loginInstructions() {
    this.log("Please enter your username:");
  }

  /**
   * Print small instructions for a user to enter a search phrase
   */
  private void searchInstructions() {
    this.log("Enter keyphrase to search by:");
  }

  /**
   * Send the users username to the facade
   * and print welcome message if a user is returned from the facade
   */
  private void login(String userName) {
    if (this.currentUser != null) {
      this.log("Please logout first...");
      return;
    }
    this.currentUser = this.api.authenticate(userName);
    if (this.currentUser == null) {
      this.log("This does not seem to be a valid username.");
      this.log("Consider typeing 'R' to register a new account.");
    } else {
      this.log("Welcome, " + userName);
    }
    this.previousInput = "";
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
   * Send a logout request to the API and clear the current user
   */
  private void logout() {
    if (this.currentUser == null) {
      this.log("Must be logged in to logout");
      return;
    }
    this.log("Goodbye " + this.currentUser.getName());
    this.currentUser = null;
  }

  /**
   * Send a search request to search the entire database using
   * using the search type and sort type that the user specifies
   */
  private Comic[] searchComics(String keyword) throws Exception {
    Comic[] comics = this.api.searchComics(1, keyword);

    this.log("Results Length: " + comics.length);
    return comics;
  }

  /**
   * Given a keyword for both a searching and sorting strategy this prepares the
   * api to correctly perform the desired search
   */
  private boolean setSearchOptions(String searchType, String sortType) {
    SearchAlgorithm searchStrategy = this.matchSearchStrategy(searchType);
    SortAlgorithm sortStrategy = this.matchSortStrategy(sortType);

    if (searchStrategy == null) {
      this.log("Invalid searchBy value: " + searchType);
      return false;
    }

    if (sortStrategy == null) {
      this.log("Invalid sortBy value: " + sortType);
      return false;
    }

    this.api.setSearchStrategy(searchStrategy);
    this.api.setSortStrategy(sortStrategy);
    return true;
  }

  /**
   * Take an inputed string and match it to a specific Search Algorithm
   * if a match cannot be made return null which specifies a wrong command
   * defaults to partial PartialKeywordSearch if empty or null string
   */
  private SearchAlgorithm matchSearchStrategy(String searchType) {
    if (searchType.equals("partial-search") || searchType.length() == 0 || searchType == null) {
      return new PartialKeywordSearch();
    }
    if (searchType.equals("exact-search")) {
      return new ExactKeywordSearch();
    }
    if (searchType.equals("exact-number")) {
      return new ExactNumberSearch();
    }
    if (searchType.equals("value")) {
      return new ValueSearch();
    }
    return null;
  }

  /**
   * Take an inputed string and match it to a specific Sort Algorithm
   * if a match cannot be made return null which specifies a wrong command
   * defaults to partial DefaultSort if empty or null string
   */
  private SortAlgorithm matchSortStrategy(String sortType) {
    if (sortType.length() == 0 || sortType == null) {
      return new DefaultSort();
    }
    if (sortType.equals("title")) {
      return new TitleSort();
    }
    if (sortType.equals("publication")) {
      return new DateSort();
    }
    if (sortType.equals("issue")) {
      return new IssueNumberSort();
    }
    if (sortType.equals("volume")) {
      return new VolumeSort();
    }
    return null;
  }

}