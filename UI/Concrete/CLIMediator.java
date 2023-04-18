package UI.Concrete;

import java.util.Scanner;

import Model.ModifiedQueue;
import Model.Command.PCCommand;
import Model.Command.ConcreteCommand.EmptyCommand;
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
    this.initCommands();
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
  public String undo() {
    try {

      if (!this.canUndo())
        return "There is no command to undo";

      PCCommand current = this.sessionCommands.getCurrent();
      String message = current.unExecute();

      if ((!message.contains("could not") || !message.contains("Could not")) && this.sessionCommands.getIndex() > 0)
        this.sessionCommands.moveBackward();

      return message;

    } catch (Exception err) {
      System.out.println("");
      System.out.println("Undo Error:\n" + err.getMessage() + "\n" + err.getLocalizedMessage());
      return "Could not undo command";
    }

  }

  @Override
  public String redo() {
    try {

      if (!this.canRedo())
        return "There is no command to redo";

      PCCommand lastUndo = this.sessionCommands.moveForward();
      String message = lastUndo.execute();

      if (message.contains("could not") || message.contains("Could not"))
        this.sessionCommands.moveBackward();

      return message;

    } catch (Exception err) {
      System.out.println("");
      System.out.println("Redo Error:\n" + err.getMessage() + "\n" + err.getLocalizedMessage());
      return "Could not redo command";
    }
  }

  @Override
  public void undoAll() {
    // int i = 1;
    // while (this.canUndo()) {
    // System.out.println("Undoing: " + i);
    // i++;
    // String message = this.undo();
    // if (message.contains("There is no"))
    // break;
    // this.cli.displayMessage(message);
    // }

    this.sessionCommands.setIteratorDirection(true);

    for (PCCommand command : this.sessionCommands) {
      String commandName = command.getClass().toGenericString();

      try {
        String message = command.unExecute();
        if (message.contains("There is no"))
          break;
        this.cli.displayMessage(message);
      } catch (Exception err) {
        this.cli.displayMessage(commandName + " could not be ran...");
      }

    }
  }

  @Override
  public void redoAll() {
    // int i = 1;
    // while (this.canRedo()) {
    // System.out.println("Redoing: " + i);
    // i++;
    // String message = this.redo();
    // if (message.contains("There is no"))
    // break;
    // this.cli.displayMessage(message);
    // }

    this.sessionCommands.setIteratorDirection(false);

    for (PCCommand command : this.sessionCommands) {
      String commandName = command.getClass().toGenericString();

      try {
        String message = command.execute();
        if (message.contains("There is no"))
          break;
        this.cli.displayMessage(message);
      } catch (Exception err) {
        this.cli.displayMessage(commandName + " could not be ran...");
      }

    }
  }

  @Override
  public void instructions() {

    cli.instructions(true);

  }

  @Override
  public Boolean canUndo() {
    // this.cli.displayMessage("Size: " + this.sessionCommands.size());
    // this.cli.displayMessage("Index: " + this.sessionCommands.getIndex());
    return this.sessionCommands.size() >= 1 && (this.sessionCommands.getIndex() >= 0);
  }

  @Override
  public Boolean canRedo() {
    return this.sessionCommands.size() - 1 > this.sessionCommands.getIndex();
  }

  @Override
  public void initCommands() {
    this.sessionCommands = new ModifiedQueue<PCCommand>();
    this.sessionCommands.addToQueue(new EmptyCommand());
  }

}
