package Model.Command.ConcreteCommand;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;

public class AddToPC implements PCCommand {

  private User user;
  private Comic comic;

  private GuestComixAPI api;

  public AddToPC(User user, Comic comic, GuestComixAPI api) {
    this.user = user;
    this.comic = comic;
    this.api = api;
  }

  @Override
  public String execute() throws Exception {

    // This should return the new comic with the correct copy id not a boolean
    Integer copyId = this.api.addComicToPersonalCollection(this.user,
        this.comic);

    if (copyId == null)
      return "Comic \"" + this.comic.getTitle() + "\" could not be added to " + this.user.getName() + "'s collection";

    this.comic.setCopyId(copyId);
    return "Comic \"" + this.comic.getTitle() + "\" successfully added to " + this.user.getName() + "'s collection";

  }

  @Override
  public String unExecute() throws Exception {
    Boolean success = this.api.removeComicFromPersonalCollection(this.user,
        this.comic);

    if (success)
      return "Comic \"" + this.comic.getTitle() + "\" successfully removed from " + this.user.getName()
          + "'s collection";

    return "Comic \"" + this.comic.getTitle() + "\" could not be removed from " + this.user.getName() + "'s collection";

  }

}
