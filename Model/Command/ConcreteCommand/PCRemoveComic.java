package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.CopyIdMaintenance.CopyIdRecord;

public class PCRemoveComic implements PCCommand {

  private User user;
  private Comic comic;
  private CopyIdRecord record;

  private GuestComixAPI api;

  public PCRemoveComic(User user, Comic comic, GuestComixAPI api, CopyIdControl cic) {
    this.user = user;
    this.comic = comic;
    this.api = api;
    this.record = cic.findOrAdd(comic.getCopyId());
  }

  @Override
  public String execute() throws Exception {
    this.record.updateComic(comic);

    Boolean success = this.api.removeComicFromPersonalCollection(this.user,
        this.comic);

    if (success)
      return "Comic \"" + this.comic.getTitle() + "\" successfully removed from " + this.user.getName()
          + "'s collection";

    return "Comic \"" + this.comic.getTitle() + "\" could not be removed from " + this.user.getName() + "'s collection";

  }

  @Override
  public String unExecute() throws Exception {
    Integer copyId = this.api.addComicToPersonalCollection(this.user,
        this.comic);

    if (copyId == null)
      return "Comic \"" + this.comic.getTitle() + "\" could not be added to " + this.user.getName() + "'s collection";
    
    this.record.setCurrentId(copyId);
    // call updateSignaturesForNewCopyId() in ComicController through API
    // update the comic's copy id
    this.record.updateComic(comic);
    return "Comic \"" + this.comic.getTitle() + "\" successfully added to " + this.user.getName() + "'s collection";

  }

}
