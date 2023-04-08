package UI.Concrete;

import Model.JavaObjects.User;
import UI.Interfaces.Mediator;

public class GuestInterpreter extends DefaultInterpreter {

  public GuestInterpreter(Mediator mediator) {
    super(mediator);
  }

  @Override
  public String interprete(String input) {

    if ((input.startsWith("R") || input.startsWith("r"))) {
      String username = input.substring(2, input.length()).trim();
      return this.register(username);
    }

    if ((input.startsWith("L") || input.startsWith("l"))) {
      String username = input.substring(2, input.length()).trim();
      return this.login(username);
    }

    return super.interprete(input);

  }

  /**
   * Communicate with the api to register a new account, then change the CLI and
   * interpreter currently being used
   * 
   * @param username The name provided is the name used to register a new account
   * @return A message to display back to the end-user, can be a success or error
   *         message
   */
  private String register(String username) {
    String errMessage = "User could not be registered with username: " + username;
    try {
      User user = this.api.register(username);

      if (user == null) {
        this.mediator.setUser(null);
        this.mediator.setCli(new GuestCLI());
        this.mediator.setInterpreter(this);
        return errMessage;
      }

      return this.login(username);

      // String successMessage = "Welcome " + user.getName() + "\nPress \"I\" to see
      // new available options";

      // this.mediator.setUser(user);
      // this.mediator.setCli(new AuthCLI());
      // this.mediator.setInterpreter(new AuthInterpreter(this.mediator, this.api));

      // return successMessage;
    } catch (Exception err) {
      errMessage = "Internal error:\n" + err.getLocalizedMessage() + "\n" + err.getMessage();
      return errMessage;
    }
  }

  /**
   * Communicate with the api to login an end-user into their account, then change
   * the CLI and interpreter currently being used
   * 
   * @param username The name provided is the name used to register a new account
   * @return A message to display back to the end-user, can be a success or error
   *         message
   */
  private String login(String username) {
    String errMessage = "User could not be found with username: " + username;
    try {
      User user = this.api.authenticate(username);

      if (user == null) {
        return errMessage;
      }

      String successMessage = "Welcome " + user.getName() + "\nPress \"I\" to see new available options";

      this.mediator.setUser(user);
      this.mediator.setCli(new AuthCLI());
      this.mediator.setInterpreter(new AuthInterpreter(this.mediator, this.api));

      return successMessage;
    } catch (Exception err) {
      errMessage = "Internal error:\n" + err.getLocalizedMessage() + "\n" + err.getMessage();
      return errMessage;
    }
  }

}
