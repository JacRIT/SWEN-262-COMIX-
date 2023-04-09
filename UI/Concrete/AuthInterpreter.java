package UI.Concrete;

import java.util.Arrays;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import UI.Interfaces.Mediator;

public class AuthInterpreter extends DefaultInterpreter {

  private Integer lastViewed;

  public AuthInterpreter(Mediator mediator, GuestComixAPI api) {
    super(mediator);
    this.api = api;
  }

  @Override
  public String interprete(String input) {

    String command = input;
    String[] flags = new String[0];

    if (this.isFlagged(input)) {
      flags = this.seperateFlags(input);
      String removing = flags[0];
      command = removing;
      flags = Arrays.stream(flags).filter((flag) -> {
        return !removing.equals(flag);
      }).toArray(String[]::new);
    }

    // View a specific comic
    if (command.startsWith("V") || command.startsWith("v")) {
      Boolean detailed = this.isComicDetailed(flags);

      if (detailed == null)
        return "Unkown flag (" + input + ") found, please try again";

      Integer comicId = this.getViewComicId(command);

      if (comicId == null)
        return "Command (" + command + ") is in an incorrect format";

      return this.viewComic(comicId, detailed);
    }

    // Browse personal collection
    if (command.startsWith("BP") || command.startsWith("bp")) {
      String personalCollection = this.browsePC();
      this.lastViewed = null;
      return personalCollection;
    }

    // Search personal collection
    if (command.startsWith("SP") || command.startsWith("sp")) {
      String flagMessage = this.setSearchFlags(flags);
      String keyword = command.substring(2, command.length()).trim();

      this.lastViewed = null;
      return this.search(keyword) + flagMessage;
    }

    // Add to personal collection
    if (command.startsWith("AP") || command.startsWith("ap")) {
      String fullCommand = command.trim();
      if (this.lastViewed != null && fullCommand.length() == 2) {
        fullCommand += " " + this.lastViewed.toString();
      }

      this.lastViewed = null;
      return this.createCommand(fullCommand);
    }

    // Remove from personal collection
    if (command.startsWith("RE") || command.startsWith("re")) {
      String fullCommand = command.trim();
      if (this.lastViewed != null && fullCommand.length() == 2) {
        fullCommand += " " + this.lastViewed.toString();
      }

      this.lastViewed = null;
      return this.createCommand(fullCommand);
    }

    // Grade comic in personal collection
    if (command.startsWith("G") || command.startsWith("g")) {
      String fullCommand = command.trim();
      if (this.lastViewed != null && fullCommand.length() == 2) {
        fullCommand += " " + this.lastViewed.toString();
      }

      this.lastViewed = null;
      return this.createCommand(fullCommand);
    }

    // Undo all commands
    if (command.startsWith("UA") || command.startsWith("ua")) {
      this.lastViewed = null;
      this.mediator.undoAll();
      return "";
    }

    // Redo all commands
    if (command.startsWith("RA") || command.startsWith("ra")) {
      this.lastViewed = null;
      this.mediator.redoAll();
      return "";
    }

    // Undo command
    if (command.startsWith("U") || command.startsWith("u")) {
      this.lastViewed = null;
      String message = this.mediator.undo();
      return message;
    }

    // Redo command
    if (command.startsWith("R") || command.startsWith("r")) {
      this.lastViewed = null;
      String message = this.mediator.redo();
      return message;
    }

    // Search entire database
    if (command.startsWith("S") || command.startsWith("s")) {
      String flagMessage = this.setSearchFlags(flags);
      String keyword = command.substring(2, command.length()).trim();

      this.lastViewed = null;
      return super.search(keyword) + flagMessage;
    }

    System.out.println("");
    System.out.println("Using super");
    System.out.println("");

    this.lastViewed = null;
    return super.interprete(input);
  }

  /**
   * Given an input, create the specified command that can be undone or redone at
   * the will of the user by adding the created command to the mediators queue of
   * commands
   * 
   * @param input A string containing the input entered by the user
   * @return A message to display back to the end-user, can be a success or error
   *         message
   */
  private String createCommand(String input) {
    try {
      PCCommand newCommand = this.commandFactory.createCommand(input, this.mediator.getUser(), this.api);

      if (newCommand == null) {
        throw new Exception("Command created was null");
      }

      String successMessage = newCommand.execute();

      if (successMessage.contains("could not"))
        return successMessage;

      // System.out.println("Command Executed Beep Boop");
      // System.out.println(successMessage);

      this.mediator.addCommand(newCommand);

      successMessage += "\nEnter \"U\" to undo this action";

      return successMessage;
    } catch (Exception err) {
      System.out.println("Create Command Err:\n" + err.getLocalizedMessage() + "\n" + err.getMessage());
      return "Command: " + input.split(" ")[0] + " could not be executed";
    }
  }

  /**
   * Communicate with the api to browse a specific users personal collection
   */
  private String browsePC() {
    try {

      System.out.println();
      System.out.println("Loading Personal Collection...");
      System.out.println();

      this.api.setSearchStrategy(new PartialKeywordSearch());
      Comic[] comics = this.api.browsePersonalCollection(this.mediator.getUser().getId());

      if (comics == null)
        return "Your personal collection is empty";

      String successMessage = this.mediator.getUser().getName() + "'s Personal Collection:";

      for (Comic comic : comics) {
        successMessage += "\n\n" + comic.toString();
      }

      return successMessage;

    } catch (Exception err) {

      System.out.println();
      System.out.println("Browse Error:\n" + err.getMessage() + "\n" + err.getLocalizedMessage());
      System.out.println();

      return "Internal Error Browsing " + this.mediator.getUser().getName() + "'s Personal Collection";

    }
  }

  /**
   * Search the entire database to find a matching comic to the keyword provided
   * that is also contained within the specified users personal collection
   * 
   * @param keyword The phrase to search the database with
   * @return A message to display back to the end-user, can be a success or error
   *         message
   */
  @Override
  protected String search(String keyword) {
    try {
      User user = this.mediator.getUser();
      Comic[] comics = this.api.searchComics(user.getId(), keyword);

      System.out.println("");
      System.out.println("Searching Personal Collection ...");
      System.out.println("");

      String success = "Results of " + keyword + ":\n";

      for (int i = 0; i < comics.length; i++) {
        Comic comic = comics[i];
        success += "\n\n" + comic.toString();
      }

      success += "Total Results: " + comics.length + "\n";

      return success;

    } catch (Exception err) {
      return "Search <" + keyword + "> Error:\n" + err.getLocalizedMessage() + "/n" + err.getMessage();
    }
  }

  @Override
  protected String viewComic(int comicId, Boolean detailed) {
    String defaultBehavior = super.viewComic(comicId, detailed);
    if (defaultBehavior.startsWith("Internal error"))
      return defaultBehavior;
    this.lastViewed = comicId;
    // String addComicInstructions = "\nEnter \"AP\" to add to your personal
    // collection.";
    String addComicInstructions = "\nEnter \"AP\" to add to your personal collection.";
    String removeComicInstructions = "\nEnter \"RE\" to remove from your personal collection.";
    String signComicInstructions = "\nEnter \"SG\" to add your signature to the comic";
    String slabComicInstructions = "\nEnter \"SL\" to slab this comic";
    String gradeComicInstructions = "\nEnter \"G <Graded Value>\" to grade this comic on a scale of 1-10";
    String authenticateInstructions = "\nEnter \"A\" to authenticate the signatures on the comic";
    return defaultBehavior + addComicInstructions + removeComicInstructions + signComicInstructions
        + slabComicInstructions
        + gradeComicInstructions + authenticateInstructions;
  }
}
