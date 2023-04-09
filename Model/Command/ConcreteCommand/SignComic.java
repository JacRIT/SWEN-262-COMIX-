package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
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
   * 
   * @throws Exception
   */
  @Override
  public String execute() throws Exception {
    Signature signature = new Signature(this.user.getId(), this.user.getName());
    Boolean success = this.api.signComic(signature, comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been signed by " + this.user.getName();
    return "Comic (" + this.comic.getTitle() + ") could not be signed by " + this.user.getName();
  }

  /**
   * Waiting on api responses
   * 
   * @throws Exception
   */
  @Override
  public String unExecute() throws Exception {
    Signature signature = new Signature(this.user.getId(), this.user.getName());
    Boolean success = this.api.signComic(signature, comic);
    if (success)
      return this.user.getName() + " signature has been removed from Comic (" + this.comic.getTitle() + ")";
    return this.user.getName() + " signature could not be removed from Comic (" + this.comic.getTitle() + ")";
  }

}
