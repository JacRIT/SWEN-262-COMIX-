package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class AddToPC implements PCCommand {

  private User user;
  private int comicId;

  private ComixCommonAPI api;

  public AddToPC(User user, int comicId, ComixCommonAPI api) {
    this.user = user;
    this.comicId = comicId;
    this.api = api;
  }

  @Override
  public void execute() {
    Comic comic = this.api.getComic(this.comicId);

    this.api.addComicToPersonalCollection(this.user, comic);
  }

  @Override
  public void unExecute() {
    Comic comic = this.api.getComic(this.comicId);

    this.api.removeComicFromPersonalCollection(this.user, comic);
  }

}
