package Model.Command.ConcreteCommand;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class SlabComic implements PCCommand {

  private User user;
  private Comic comic;
  private ComixCommonAPI api;

  public SlabComic(User user,
      ComixCommonAPI api, Comic comic) {
    this.user = user;
    this.comic = comic;
    this.api = api;
  }

  @Override
  public String execute() {
    // Comic comic = this.api.getComic(this.comicId);

    // this.api.slabGradedComicInPersonalCollection(this.user, comic);

    return "Comic (" + this.comic.getTitle() + ") has been slabbed";
  }

  @Override
  public String unExecute() {
    // Comic comic = this.api.getComic(this.comicId);

    // this.api.unslabGradedComicInPersonalCollection(this.user, comic);

    return "Comic (" + this.comic.getTitle() + ") has been unslabbed";
  }

}
