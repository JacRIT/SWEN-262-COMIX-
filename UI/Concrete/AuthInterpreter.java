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
      String flagMessage = this.setFlags(flags);

      String keyword = command.substring(2, command.length()).trim();
      return this.search(keyword) + flagMessage;
    }

    if (command.startsWith("A") || command.startsWith("a")) {
      PCCommand newCommand = this.commandFactory.createAlgorithim(command);
      String successMessage = newCommand.execute();
      this.mediator.addCommand(newCommand);
      return successMessage;
    }

    return super.interprete(input);
  }

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
