package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;

public class AuthenticateComic implements PCCommand {

  // private User user;
  private Comic comic;
  private Signature signature;

  private GuestComixAPI api;

  public AuthenticateComic(Signature signature, User user, Comic comic, GuestComixAPI api) {
    // this.user = user;
    this.comic = comic;
    this.api = api;
    this.signature = signature;
  }

  @Override
  public String execute() throws Exception {
    Signature signed = this.api.verifyComic(this.signature, this.comic);
    if (signed == null)
      return "Comic (" + this.comic.getTitle() + ") could not be verified";
    return "Comic (" + this.comic.getTitle() + ") has been verified";
  }

  @Override
  public String unExecute() throws Exception {
    Signature s = this.api.unVerifyComic(this.signature, this.comic);
    if (s == null)
      return "Comic (" + this.comic.getTitle() + ") could not be unverified";
    this.signature = s;
    return "Comic (" + this.comic.getTitle() + ") has been unverified";

  }

}
