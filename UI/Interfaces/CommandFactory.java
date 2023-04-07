package UI.Interfaces;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.User;

public interface CommandFactory {

  public PCCommand createCommand(String type, User user, ComixCommonAPI api);
}
