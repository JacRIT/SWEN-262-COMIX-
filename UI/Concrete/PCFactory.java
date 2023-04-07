package UI.Concrete;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.Command.ConcreteCommand.AddToPC;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.Interfaces.CommandFactory;

public class PCFactory implements CommandFactory {

  @Override
  public PCCommand createCommand(String type, User user, ComixCommonAPI api) {

    if (type.startsWith("A")) {
      String[] split = type.split(" ");
      if (split.length != 2)
        return null;

      int comicId = Integer.parseInt(split[1]);
      Comic comic = api.getComic(comicId);
      return new AddToPC(user, comic, api);
    }

    return null;
  }

}
