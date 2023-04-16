package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.CopyIdMaintenance.CopyIdRecord;

public class AddToPC implements PCCommand {

  private User user;
  private Comic comic;
  private CopyIdControl cic;
  private CopyIdRecord record;

  private GuestComixAPI api;

  public AddToPC(User user, Comic comic, GuestComixAPI api, CopyIdControl cic) {
    this.user = user;
    this.comic = comic;
    this.api = api;
    this.cic = cic;
    this.record = null;
  }

  @Override
  public String execute() throws Exception {

    // This should return the new comic with the correct copy id not a boolean
    Integer copyId = this.api.addComicToPersonalCollection(this.user,
        this.comic);

    if (copyId == null)
      return "Comic \"" + this.comic.getTitle() + "\" could not be added to " + this.user.getName() + "'s collection";

    // if this is the first time execute has run
    if (record == null) {
      this.record = this.cic.addRecord(copyId);
    } else {
      api.updateSignatureRefrences(comic.getCopyId(), copyId);
      this.record.setCurrentId(copyId);
    }
    // update the comic's copy id
    this.record.updateComic(comic);

    return "Comic \"" + this.comic.getTitle() + "\" successfully added to " + this.user.getName() + "'s collection";

  }

  @Override
  public String unExecute() throws Exception {
    this.record.updateComic(comic);

    Boolean success = this.api.removeComicFromPersonalCollection(this.user,
        this.comic);

    if (success)
      return "Comic \"" + this.comic.getTitle() + "\" successfully removed from " + this.user.getName()
          + "'s collection";

    return "Comic \"" + this.comic.getTitle() + "\" could not be removed from " + this.user.getName() + "'s collection";

  }

}
