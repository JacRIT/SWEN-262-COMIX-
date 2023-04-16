package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.CopyIdMaintenance.CopyIdRecord;

public class AuthenticateComic implements PCCommand {

  // private User user;
  private Comic comic;
  private Signature signature;
  private CopyIdRecord record;

  private GuestComixAPI api;

  public AuthenticateComic(Signature signature, User user, Comic comic, GuestComixAPI api, CopyIdControl cic) {
    // this.user = user;
    this.comic = comic;
    this.api = api;
    this.signature = signature;
    this.record = cic.findOrAdd(comic.getCopyId());
  }

  @Override
  public String execute() throws Exception {
    this.record.updateComic(comic);
    Signature signed = this.api.verifyComic(this.signature, this.comic);
    if (signed == null)
      return "Comic (" + this.comic.getTitle() + ") could not be verified";
    this.signature = signed;
    return "Comic (" + this.comic.getTitle() + ") has been verified";
  }

  @Override
  public String unExecute() throws Exception {
    this.record.updateComic(comic);
    Signature s = this.api.unVerifyComic(this.signature, this.comic);
    if (s == null)
      return "Comic (" + this.comic.getTitle() + ") could not be unverified";
    this.signature = s;
    return "Comic (" + this.comic.getTitle() + ") has been unverified";

  }

}
