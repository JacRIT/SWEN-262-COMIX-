package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class GradeComic implements PCCommand {

  private int newGrade;
  private Comic oldComic;
  private User user;

  private ComixCommonAPI api;

  public GradeComic(int lastGrade, int newGrade, Comic oldComic, User user, ComixCommonAPI api) {
    this.newGrade = newGrade;
    this.oldComic = oldComic;
    this.user = user;
    this.api = api;
  }

  @Override
  public String execute() {
    this.api.gradeComicInPersonalCollection(this.user, this.oldComic, this.newGrade);
    return "";
  }

  @Override
  public String unExecute() {
    this.api.gradeComicInPersonalCollection(this.user, this.oldComic, this.oldComic.getGrade());
    return "";
  }

}
