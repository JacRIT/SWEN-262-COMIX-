package UI.Concrete;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.Command.ConcreteCommand.AddToPC;
import Model.Command.ConcreteCommand.GradeComic;
import Model.Command.ConcreteCommand.SignComic;
import Model.Command.ConcreteCommand.SlabComic;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import UI.Interfaces.CommandFactory;

public class PCFactory implements CommandFactory {

  @Override
  public PCCommand createCommand(String type, User user, ComixCommonAPI api) throws Exception {

    String[] split = type.split(" ");

    try {

      System.out.println();
      System.out.println("Command Factory: " + type);
      System.out.println();

      if (type.startsWith("AP") || type.startsWith("ap")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new AddToPC(user, comic, api);
      }

      if (type.startsWith("SG") || type.startsWith("sg")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new SignComic(user, comic, api);
      }

      if (type.startsWith("SL") || type.startsWith("sl")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new SlabComic(user, comic, api);
      }

      if (type.startsWith("G") || type.startsWith("g")) {
        if (split.length != 3)
          return null;
        int grade = Integer.parseInt(split[1]);
        int comicId = Integer.parseInt(split[2]);
        Comic comic = api.getComic(comicId);
        return new GradeComic(grade, user, comic, api);
      }

      return null;

    } catch (Exception err) {
      System.out.println();
      System.out.println("Command Factory Error: \n" + err.getMessage() + "\n" + err.getLocalizedMessage());
      System.out.println();
      return null;
    }
  }

}
