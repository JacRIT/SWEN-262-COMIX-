package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class AuthenticateComic implements PCCommand {

  private User user;
  private Comic comic;

  private ComixCommonAPI api;

  public AuthenticateComic(User user, Comic comic, ComixCommonAPI api) {
    this.user = user;
    this.comic = comic;
    this.api = api;
  }

  @Override
  public String execute() {
    // this.api.verifyComic(this.user, this.comic);
    return "Comic (" + this.comic.getTitle() + ") has been verified";
  }

  @Override
  public String unExecute() {
    // this.api.unVerifyComic(this.user, this.comic);
    return "Comic (" + this.comic.getTitle() + ") has been unverified";
  }

}
