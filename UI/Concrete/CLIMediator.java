package UI.Concrete;

import java.util.Scanner;

import Model.ModifiedQueue;
import Model.Command.PCCommand;
import Model.JavaObjects.User;
import UI.Interfaces.CLI;
import UI.Interfaces.Interpreter;
import UI.Interfaces.Mediator;

public class CLIMediator implements Mediator {

  private ModifiedQueue<PCCommand> sessionCommands;
  private CLI cli;
  private Interpreter reader;
  private User user;

  public CLIMediator() {
    this.cli = new GuestCLI();
    this.reader = new GuestInterpreter(this);
    this.sessionCommands = new ModifiedQueue<PCCommand>();
  }

  @Override
  public void mediate(Scanner scanner) {

    String input = cli.readInput(scanner);

    String message = reader.interprete(input);

    if (message == null) {
      cli.displayMessage("GoodBye");
      return;
    }

    if (message.length() > 0)
      cli.displayMessage(message);

    // return true;

    this.mediate(scanner);
  }

  @Override
  public void setCli(CLI client) {
    this.cli = client;
  }

  @Override
  public void setInterpreter(Interpreter reader) {
    this.reader = reader;
  }

  @Override
  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public User getUser() {
    return this.user;
  }

  @Override
  public void addCommand(PCCommand command) {

    this.sessionCommands.addToQueue(command);

  }

  @Override
  public void undo() {

    PCCommand lastCommand = this.sessionCommands.moveBackward();
    lastCommand.unExecute();

  }

  @Override
  public void redo() {

    PCCommand lastCommand = this.sessionCommands.getCurrent();
    lastCommand.execute();
    this.sessionCommands.moveForward();

  }

  @Override
  public void undoAll() {

    // Iterable goingBack = this.sessionCommands.backwardIterator();

    // for (PCCommand command : goingBack) {

    // }

  }

  @Override
  public void redoAll() {
    // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'redoAll'");
  }

  @Override
  public void instructions() {

    cli.instructions(true);

  }

}
