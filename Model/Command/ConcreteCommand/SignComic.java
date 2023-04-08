package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class SignComic implements PCCommand {

  private User user;
  private Comic comic;

  private ComixCommonAPI api;

  public SignComic(User user, Comic comic, ComixCommonAPI api) {
    this.user = user;
    this.comic = comic;
    this.api = api;
  }

  /**
   * Waiting on api responses
   */
  @Override
  public String execute() {

    return "Comic (" + this.comic.getTitle() + ") has been signed by " + this.user.getName();
  }

  /**
   * Waiting on api responses
   */
  @Override
  public String unExecute() {

    return this.user.getName() + " signature has been removed from Comic (" + this.comic.getTitle() + ")";
  }

}
