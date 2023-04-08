package UI.Concrete;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.Command.ConcreteCommand.AddToPC;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.Interfaces.CommandFactory;

public class PCFactory implements CommandFactory {

  @Override
  public PCCommand createCommand(String type, User user, ComixCommonAPI api) throws Exception {

    System.out.println();
    System.out.println("Command Factory: " + type);
    System.out.println();

    if (type.startsWith("AP") || type.startsWith("ap")) {
      String[] split = type.split(" ");
      if (split.length != 2)
        return null;

      try {
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new AddToPC(user, comic, api);

      } catch (Exception err) {
        System.out.println();
        System.out.println("Command Factory Error: \n" + err.getMessage() + "\n" + err.getLocalizedMessage());
        System.out.println();
        return null;
      }
    }

    return null;
  }

}
