package UI.Interfaces;

import java.util.Scanner;

import Model.Command.PCCommand;
import Model.JavaObjects.User;

public interface Mediator {

  public void mediate(Scanner scanner);

  public void setCli(CLI client);

  public void setInterpreter(Interpreter reader);

  public void setUser(User user);

  public User getUser();

  public void addCommand(PCCommand command);

  public void instructions();

  public void undo();

  public void redo();

  public void undoAll();

  public void redoAll();

}
