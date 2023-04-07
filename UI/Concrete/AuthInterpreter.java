package UI.Concrete;

import java.util.Arrays;

import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.Interfaces.Mediator;

public class AuthInterpreter extends DefaultInterpreter {

  public AuthInterpreter(Mediator mediator) {
    super(mediator);
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

    if (command.startsWith("SP") || command.startsWith("sp")) {
      String flagMessage = this.setSearchFlags(flags);

      String keyword = command.substring(2, command.length()).trim();
      return this.search(keyword) + flagMessage;
    }

    if (command.startsWith("A") || command.startsWith("a")) {
      return this.createCommand(command);
    }

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
      String successMessage = newCommand.execute();
      this.mediator.addCommand(newCommand);
      return successMessage;
    } catch (Exception err) {
      System.out.println("Create Command Err:\n" + err.getLocalizedMessage() + "\n" + err.getMessage());
      return "Command: " + input.split(" ")[0] + " could not be executed";
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
        success += "\t" + comic.toString();
        success += "\n\n";
      }

      success += "Total Results: " + comics.length + "\n";

      return success;

    } catch (Exception err) {
      return "Search <" + keyword + "> Error:\n" + err.getLocalizedMessage() + "/n" + err.getMessage();
    }
  }

}
