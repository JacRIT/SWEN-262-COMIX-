package Model.Command.ConcreteCommand;

import Model.Command.PCCommand;

public class EmptyCommand implements PCCommand {

  public EmptyCommand() {

  }

  @Override
  public String execute() throws Exception {
    return "There is no command that can be redone";
  }

  @Override
  public String unExecute() throws Exception {
    return "There is no command that can be done";
  }

}
