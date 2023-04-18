package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class CreateComic implements PCCommand {

  private GuestComixAPI api;
  private User user;
  private Comic comic;

  public CreateComic(User user, Comic comic, GuestComixAPI api) {
    this.api = api;
    this.user = user;
    this.comic = comic;
  }

  @Override
  public String execute() throws Exception {

    // Comic object should be returned rather than just the copy id
    int copyId = this.api.createComic(this.user.getId(), this.comic);

    return "Comic (" + this.comic.getTitle() + ") created";
  }

  @Override
  public String unExecute() throws Exception {
    return "Comic creation is not an undoable action...\n\nThis comics info:\n" + this.comic.toStringDetailed()
        + "\n will be stored with us FOREVER!!\nMWUHAHAHAHAHAHAHA";
  }

}
