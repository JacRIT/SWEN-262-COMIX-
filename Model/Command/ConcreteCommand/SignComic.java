package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.User;

public class SignComic implements PCCommand {

  private User user;
  private int comicId;

  private ComixCommonAPI api;

  public SignComic(User user, int comicId, ComixCommonAPI api) {
    this.user = user;
    this.comicId = comicId;
    this.api = api;
  }

  /**
   * Waiting on api responses
   */
  @Override
  public void execute() {

  }

  /**
   * Waiting on api responses
   */
  @Override
  public void unExecute() {

  }

}
