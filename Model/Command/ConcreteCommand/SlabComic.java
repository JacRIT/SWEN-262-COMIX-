package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.CopyIdMaintenance.CopyIdRecord;

public class SlabComic implements PCCommand {

  private User user;
  private Comic comic;
  private GuestComixAPI api;
  private CopyIdRecord record;

  public SlabComic(User user,
      Comic comic, GuestComixAPI api, CopyIdControl cic) {
    this.user = user;
    this.comic = comic;
    this.api = api;
    this.record = cic.findOrAdd(comic.getCopyId());
  }

  @Override
  public String execute() throws Exception {
    this.record.updateComic(comic);
    Boolean success = this.api.slabGradedComicInPersonalCollection(this.user, comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been slabbed";
    return "Comic (" + this.comic.getTitle() + ") could not be slabbed";
  }

  @Override
  public String unExecute() throws Exception {
    this.record.updateComic(comic);
    Boolean success = this.api.unslabGradedComicInPersonalCollection(this.user, comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been unslabbed";
    return "Comic (" + this.comic.getTitle() + ") could not be unslabbed";
  }

}
