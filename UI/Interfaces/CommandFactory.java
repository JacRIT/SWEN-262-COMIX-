package UI.Interfaces;

import Api.ComixCommonAPI;
import Model.Command.PCCommand;
import Model.JavaObjects.User;

public interface CommandFactory {

  /**
   * Instantiate the correct command based upon the input given
   * 
   * @param type String representation of the name of the algorithim to be
   *             created
   * @param user The user object of who is creating the command
   * @param api  The Comix api required to execute all commands
   * 
   * @return Personal Collection Command Object
   */
  public PCCommand createCommand(String type, User user, ComixCommonAPI api) throws Exception;

}
