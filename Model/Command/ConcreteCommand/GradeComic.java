package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.CopyIdMaintenance.CopyIdRecord;

public class GradeComic implements PCCommand {

  private int newGrade;
  private Comic comic;
  private User user;
  private CopyIdRecord record;

  private GuestComixAPI api;

  public GradeComic(int newGrade, User user, Comic comic, GuestComixAPI api, CopyIdControl cic) {
    this.newGrade = newGrade;
    this.comic = comic;
    this.user = user;
    this.api = api;
    this.record = cic.findOrAdd(comic.getCopyId());
  }

  @Override
  public String execute() throws Exception {
    this.record.updateComic(comic);
    Boolean success = this.api.gradeComicInPersonalCollection(this.user, this.comic,
        this.newGrade);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been graded at " + this.newGrade;
    return "Comic (" + this.comic.getTitle() + ") could not graded at " + this.newGrade;
  }

  @Override
  public String unExecute() throws Exception {
    this.record.updateComic(comic);
    Boolean success = this.api.ungradeComicInPersonalCollection(this.user, this.comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been ungraded at " + this.comic.getGrade();
    return "Comic (" + this.comic.getTitle() + ") could not be ungraded at " + this.comic.getGrade();
  }

}
