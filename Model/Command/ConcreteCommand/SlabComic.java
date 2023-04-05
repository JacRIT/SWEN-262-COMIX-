package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class SlabComic implements PCCommand {

  private User user;
  private int comicId;
  private ComixCommonAPI api;

  public SlabComic(User user,
      ComixCommonAPI api, int comicId) {
    this.user = user;
    this.comicId = comicId;
    this.api = api;
  }

  @Override
  public void execute() {
    Comic comic = this.api.getComic(this.comicId);

    this.api.slabGradedComicInPersonalCollection(this.user, comic);
  }

  @Override
  public void unExecute() {
    Comic comic = this.api.getComic(this.comicId);

    this.api.unslabGradedComicInPersonalCollection(this.user, comic);
  }

}
