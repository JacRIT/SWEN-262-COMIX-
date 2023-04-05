package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class GradeComic implements PCCommand {

  private int lastGrade;
  private int newGrade;
  private int comicId;
  private User user;

  private ComixCommonAPI api;

  public GradeComic(int lastGrade, int newGrade, int comicId, User user, ComixCommonAPI api) {
    this.lastGrade = lastGrade;
    this.newGrade = newGrade;
    this.comicId = comicId;
    this.user = user;
    this.api = api;
  }

  @Override
  public void execute() {
    Comic comic = this.api.getComic(this.comicId);

    this.api.gradeComicInPersonalCollection(this.user, comic, this.newGrade);

  }

  @Override
  public void unExecute() {
    Comic comic = this.api.getComic(this.comicId);

    this.api.gradeComicInPersonalCollection(this.user, comic, this.lastGrade);

  }

}
