package UI.Concrete;

import java.util.Arrays;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import UI.Interfaces.Factory;
import UI.Interfaces.Interpreter;
import UI.Interfaces.Mediator;

public abstract class DefaultInterpreter implements Interpreter {

  protected ComixCommonAPI api;
  protected Mediator mediator;
  protected Factory<SearchAlgorithm> searchFactory;
  protected Factory<SortAlgorithm> sortFactory;
  protected Factory<PCCommand> commandFactory;

  public DefaultInterpreter(Mediator mediator) {
    try {
      this.api = new ComixCommonAPI();
    } catch (Exception e) {
      System.out.println("Server could not connect");
    }
    this.searchFactory = new SearchFactory();
    this.sortFactory = new SortFactory();
    this.mediator = mediator;
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

    if (command.startsWith("I") || command.startsWith("i")) {
      this.mediator.instructions();
      return "";
    }

    if (command.startsWith("S") || command.startsWith("s")) {

      String flagMessage = this.setFlags(flags);

      String keyword = command.substring(2, command.length()).trim();
      return this.search(keyword) + flagMessage;
    }

    if (command.startsWith("Exit") || command.startsWith("exit"))
      return null;

    return "Unrecognized Command: " + input + "\nPlease Try Again";

  }

  protected String search(String keyword) {
    try {
      Comic[] comics = this.api.searchComics(1, keyword);

      System.out.println("");
      System.out.println("Searching...");
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

  protected String setFlags(String[] flags) {
    String successMessage = "";

    SortAlgorithm sort = this.sortFactory.createAlgorithim(null);
    SearchAlgorithm search = this.searchFactory.createAlgorithim(null);

    if (flags.length == 0) {
      successMessage = "Results sorted by: DefaultSort\nResults searched by: PartialSort";
    } else
      for (String unseperated : flags) {
        System.out.println("");
        System.out.println("Looping: " + unseperated);
        System.out.println("");
        String[] seperated = unseperated.split("=");
        if (seperated.length != 2) {
          successMessage += "Flag " + seperated[0] + " is in an incorrect format.\n";
          continue;
        }

        String flagType = seperated[0];
        String flag = seperated[1];

        if (this.sortFactory.createAlgorithim(flag) != null) {
          sort = this.sortFactory.createAlgorithim(flag);
          successMessage += "Results sorted by: " + flag + "\n";
        }

        if (flagType.equals("sortBy") && this.sortFactory.createAlgorithim(flag) == null) {
          successMessage += "Flag " + seperated[0] + " provided with invalid value: " + flag;
        }

        if (this.searchFactory.createAlgorithim(flag) != null) {
          search = this.searchFactory.createAlgorithim(flag);
          successMessage += "Results searched by: " + flag + "\n";
        }

        if (flagType.equals("searchBy") && this.searchFactory.createAlgorithim(flag) == null) {
          successMessage += "Flag " + seperated[0] + " provided with invalid value: " + flag + "\n";
        }
      }

    System.out.println("Success: " + successMessage);

    this.api.setSearchStrategy(search);
    this.api.setSortStrategy(sort);

    return successMessage;

  }

  protected Boolean isFlagged(String input) {
    return this.seperateFlags(input).length > 1;
  }

  protected String[] seperateFlags(String input) {
    String[] flags = input.split("--");
    flags = Arrays.stream(flags).map((flag) -> {
      return flag.trim();
    }).toArray(String[]::new);
    return flags;
  }

}
