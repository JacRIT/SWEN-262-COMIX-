package UI.Concrete;

import java.util.Arrays;

import Api.GuestComixAPI;
import Model.JavaObjects.Comic;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import UI.Interfaces.CommandFactory;
import UI.Interfaces.Factory;
import UI.Interfaces.Interpreter;
import UI.Interfaces.Mediator;

public abstract class DefaultInterpreter implements Interpreter {

  protected GuestComixAPI api;
  protected Mediator mediator;
  protected Factory<SearchAlgorithm> searchFactory;
  protected Factory<SortAlgorithm> sortFactory;
  protected CommandFactory commandFactory;

  public DefaultInterpreter(Mediator mediator) {
    try {
      this.api = new GuestComixAPI();
    } catch (Exception e) {
      System.out.println("Server could not connect");
    }
    this.searchFactory = new SearchFactory();
    this.sortFactory = new SortFactory();
    this.commandFactory = new PCFactory();
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

      String flagMessage = this.setSearchFlags(flags);

      String keyword = command.substring(2, command.length()).trim();
      return this.search(keyword) + flagMessage;
    }

    if (command.startsWith("V") || command.startsWith("v")) {
      Boolean detailed = this.isComicDetailed(flags);

      if (detailed == null)
        return "Unkown flag (" + input + ") found, please try again";

      Integer comicId = this.getViewComicId(command);

      if (comicId == null)
        return "Command (" + command + ") is in an incorrect format";

      return this.viewComic(comicId, detailed);
    }

    if (command.startsWith("Exit") || command.startsWith("exit"))
      return null;

    return "Unrecognized Command: " + input + "\nPlease Try Again";

  }

  /**
   * Search the entire database to find a matching comic to the keyword provided
   * 
   * @param keyword The phrase to search the database with
   * @return A message to display back to the end-user, can be a success or error
   *         message
   */
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

  /**
   * Search the entire database to find a specific comic that matches the
   * specified id
   * 
   * @param comicId  The specific id of the comic a user wishes to view
   * @param detailed Boolean to decide if the comic should display a summary or
   *                 it's entire contents
   * @return A message to display back to the end-user, can be a success or error
   *         message
   */
  protected String viewComic(int comicId, Boolean detailed) {
    try {
      Comic comic = this.api.getComic(comicId);

      if (detailed)
        return comic.toStringDetailed();

      return comic.toString();
    } catch (Exception err) {
      String errMessage = "Internal error:\n" + err.getLocalizedMessage() + "\n" + err.getMessage();
      return errMessage;
    }
  }

  /**
   * Set the Search and Sort algorithims specified to search for comics
   * 
   * @param flags Collection of strings specified by the user with flags to define
   *              how to search for comics
   * @return A message to display back to the end-user for how they have decided
   *         to search for comics, can be a success or error
   *         message
   */
  protected String setSearchFlags(String[] flags) {
    String successMessage = "";

    String sortKey = "";
    String searchKey = "";
    for (String flag : flags) {
      if ( flag.startsWith("searchBy=") ) {
        searchKey = flag.split("=")[1] ;
      } else if ( flag.startsWith("sortBy=") ) {
        sortKey = flag.split("=")[1] ;
      }
    }

    SortAlgorithm sort = this.sortFactory.createAlgorithim(sortKey);
    SearchAlgorithm search = this.searchFactory.createAlgorithim(searchKey);

    if (flags.length == 0) {
      successMessage = "Results sorted by: " + sort.getClass() + "\nResults searched by: " + search.getClass();
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
          successMessage += "Results sorted by: " + sort.getClass() + "\n";
        }

        if (flagType.equals("sortBy") && this.sortFactory.createAlgorithim(flag) == null) {
          successMessage += "Flag " + seperated[0] + " provided with invalid value: " + flag;
        }

        if (this.searchFactory.createAlgorithim(flag) != null) {
          search = this.searchFactory.createAlgorithim(flag);
          successMessage += "Results searched by: " + search.getClass() + "\n";
        }

        if (flagType.equals("searchBy") && this.searchFactory.createAlgorithim(flag) == null) {
          successMessage += "Flag " + seperated[0] + " provided with invalid value: " + flag + "\n";
        }
      }

    this.api.setSearchStrategy(search);
    this.api.setSortStrategy(sort);

    return successMessage;

  }

  /**
   * Decides if the given input contains any flags within it
   * 
   * @param input The entire input entered by the user
   * @return True if the input contains any flags, False otherwise
   */
  protected Boolean isFlagged(String input) {
    return this.seperateFlags(input).length > 1;
  }

  /**
   * Decide if a comic is detailed
   * 
   * @param flags Flags entered in with the command
   */
  protected Boolean isComicDetailed(String[] flags) {
    Boolean detailed = false;
    for (String flag : flags) {
      if (flag.equals("d"))
        detailed = true;
      else
        return null;
    }
    return detailed;
  }

  /**
   * Get view comic id from string
   */
  protected Integer getViewComicId(String command) {
    String[] split = command.split(" ");

    if (split.length != 2)
      return null;

    int comicId = Integer.parseInt(split[1].trim());
    return comicId;
  }

  /**
   * Given an input, seperate the regular command from the flags included at the
   * end
   * 
   * @param input The entire input entered by the user
   * @returns A collection of strings the first index being the base command and
   *          the following containing any flags optionally entered
   */
  protected String[] seperateFlags(String input) {
    String[] flags = input.split("--");
    flags = Arrays.stream(flags).map((flag) -> {
      return flag.trim();
    }).toArray(String[]::new);
    return flags;
  }

}
