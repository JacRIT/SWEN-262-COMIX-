package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.CopyIdMaintenance.CopyIdRecord;

public class UnSignComic implements PCCommand {
  private User user;
  private Comic comic;
  private Signature signature;
  private GuestComixAPI api;
  private CopyIdRecord record;

  public UnSignComic(Signature signature, User user, Comic comic, GuestComixAPI api, CopyIdControl cic) {
    this.user = user;
    this.comic = comic;
    this.api = api;
    this.signature = signature;
    this.record = cic.findOrAdd(comic.getCopyId());
  }

  /**
   * Waiting on api responses
   * 
   * @throws Exception
   */
  @Override
  public String unExecute() throws Exception {
    this.record.updateComic(comic);
    Signature signed = this.api.signComic(this.signature, comic);

    if (signed == null)
      return "Comic (" + this.comic.getTitle() + ") could not be signed by " + this.signature.getName();

    this.signature = signed;

    return "Comic (" + this.comic.getTitle() + ") has been signed by " + this.signature.getName();
  }

  /**
   * Waiting on api responses
   * 
   * @throws Exception
   */
  @Override
  public String execute() throws Exception {
    this.record.updateComic(comic);
    if (this.signature == null)
      return "Comic (" + comic.getTitle() + ") must first be signed";

    Boolean unsigned = this.api.unSignComic(this.signature, comic);

    if (!unsigned)
      return "Signature (" + this.signature.getId() + ") could not be removed from Comic (" + this.comic.getTitle()
          + ")";

    return "Signature (" + this.signature.getId() + ") has been removed from Comic (" + this.comic.getTitle() + ")";
  }

}
