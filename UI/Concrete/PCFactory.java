package UI.Concrete;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.Command.ConcreteCommand.AddToPC;
import Model.Command.ConcreteCommand.AuthenticateComic;
import Model.Command.ConcreteCommand.GradeComic;
import Model.Command.ConcreteCommand.PCRemoveComic;
import Model.Command.ConcreteCommand.SignComic;
import Model.Command.ConcreteCommand.SlabComic;
import Model.Command.ConcreteCommand.UnSignComic;
import Model.Command.ConcreteCommand.UnslabComic;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import UI.CopyIdMaintenance.CopyIdControl;
import UI.Interfaces.CommandFactory;

public class PCFactory implements CommandFactory {

    private CopyIdControl cic;

    public PCFactory() {
      this.cic = new CopyIdControl();
    }

  @Override
  public PCCommand createCommand(String type, User user, GuestComixAPI api) throws Exception {

    String[] split = type.split(" ");

    try {

      System.out.println();
      System.out.println("Command Factory: " + type);
      System.out.println();

      // Add to personal collection
      if (type.startsWith("AP") || type.startsWith("ap")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new AddToPC(user, comic, api, this.cic);
      }

      // Authenticate a signature
      if (type.startsWith("AU") || type.startsWith("au")) {
        if (split.length != 3)
          return null;
        int signatureId = Integer.parseInt(split[1]);
        int comicId = Integer.parseInt(split[2]);
        Comic comic = api.getComic(comicId);
        Signature signature = null;

        for (Signature checking : comic.getSignatures()) {
          if (checking.getId() == signatureId)
            signature = checking;
        }

        if (signature == null)
          return null;

        System.out.println(signature.toString());

        return new AuthenticateComic(signature, user, comic, api, cic);
      }

      // Remove from personal collection
      if (type.startsWith("RE") || type.startsWith("re")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new PCRemoveComic(user, comic, api, cic);
      }

      // Add signature to comic
      if (type.startsWith("SG") || type.startsWith("sg")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new SignComic(user, comic, api, cic);
      }

      // Remove signature from comic
      if (type.startsWith("RS") || type.startsWith("rs")) {
        if (split.length != 3)
          return null;
        int signatureId = Integer.parseInt(split[1]);
        int comicId = Integer.parseInt(split[2]);
        Comic comic = api.getComic(comicId);
        Signature signature = null;

        for (Signature checking : comic.getSignatures()) {
          if (checking.getId() == signatureId)
            signature = checking;
        }

        if (signature == null)
          return null;

        System.out.println(signature.toString());

        return new UnSignComic(signature, user, comic, api, cic);
      }

      // Slab a comic
      if (type.startsWith("SL") || type.startsWith("sl")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new SlabComic(user, comic, api, cic);
      }

      // Unslab a comic
      if (type.startsWith("USL") || type.startsWith("usl")) {
        if (split.length != 2)
          return null;
        int comicId = Integer.parseInt(split[1]);
        Comic comic = api.getComic(comicId);
        return new UnslabComic(user, comic, api, cic);
      }

      // Grade a comic
      if (type.startsWith("G") || type.startsWith("g")) {
        if (split.length != 3)
          return null;
        int grade = Integer.parseInt(split[1]);
        int comicId = Integer.parseInt(split[2]);
        Comic comic = api.getComic(comicId);
        return new GradeComic(grade, user, comic, api, cic);
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
