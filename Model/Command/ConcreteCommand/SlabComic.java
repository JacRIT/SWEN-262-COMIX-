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
      Comic comic, ComixCommonAPI api) {
    this.user = user;
    this.comic = comic;
    this.api = api;
  }

  @Override
  public String execute() throws Exception {
    Boolean success = this.api.slabGradedComicInPersonalCollection(this.user, comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been slabbed";
    return "Comic (" + this.comic.getTitle() + ") could not be slabbed";
  }

  @Override
  public String unExecute() throws Exception {
    Boolean success = this.api.unslabGradedComicInPersonalCollection(this.user, comic);
    if (success)
      return "Comic (" + this.comic.getTitle() + ") has been unslabbed";
    return "Comic (" + this.comic.getTitle() + ") could not be unslabbed";
  }

}
