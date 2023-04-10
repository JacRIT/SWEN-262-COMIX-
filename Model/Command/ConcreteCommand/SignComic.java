package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;

public class SignComic implements PCCommand {

  private User user;
  private Comic comic;
  private Signature signature;
  private GuestComixAPI api;

  public SignComic(User user, Comic comic, GuestComixAPI api) {
    this.user = user;
    this.comic = comic;
    this.api = api;
    this.signature = null;
  }

  /**
   * Waiting on api responses
   * 
   * @throws Exception
   */
  @Override
  public String execute() throws Exception {
    Signature base = new Signature(this.user.getId(), this.user.getName());
    Signature signed = this.api.signComic(base, comic);

    if (signed == null)
      return "Comic (" + this.comic.getTitle() + ") could not be signed by " + this.user.getName();

    this.signature = signed;

    return "Comic (" + this.comic.getTitle() + ") has been signed by " + this.user.getName();
  }

  /**
   * Waiting on api responses
   * 
   * @throws Exception
   */
  @Override
  public String unExecute() throws Exception {
    if (this.signature == null)
      return "Comic (" + comic.getTitle() + ") must first be signed";

    Signature signed = this.api.signComic(this.signature, comic);

    if (signed == null)
      return this.user.getName() + " signature could not be removed from Comic (" + this.comic.getTitle() + ")";

    return this.user.getName() + " signature has been removed from Comic (" + this.comic.getTitle() + ")";
  }

}
