package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
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
  public String execute() throws Exception {
    Signature signature = new Signature(this.user.getId(), this.user.getName());
    Boolean success = this.api.verifyComic(signature, this.comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been verified";
    return "Comic (" + this.comic.getTitle() + ") could not be verified";
  }

  @Override
  public String unExecute() throws Exception {
    Signature signature = new Signature(this.user.getId(), this.user.getName());
    Boolean success = this.api.verifyComic(signature, this.comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been unverified";
    return "Comic (" + this.comic.getTitle() + ") could not be unverified";

  }

}
