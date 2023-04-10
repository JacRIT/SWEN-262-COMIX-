package UI.Interfaces;

import java.util.Scanner;

import Model.Command.PCCommand;
import Model.JavaObjects.User;

public interface Mediator {

  /**
   * Entry point of the mediator, initiates contact between colleagues
   * 
   * @param scanner to allow the cli to accept input from the command line
   */
  public void mediate(Scanner scanner);

  /**
   * Changes the current instance of the cli
   * 
   * @param cli new cli instance to be used going forward
   */
  public void setCli(CLI client);

  /**
   * Changes the current instance of the Interpreter
   * 
   * @param reader new Interpreter instance to be used going forward
   */
  public void setInterpreter(Interpreter reader);

  /**
   * Changes the current instance of the user
   * 
   * @param user new user instance to be used going forward
   */
  public void setUser(User user);

  /**
   * Get the current instance of the user
   * 
   * @return current instance of the user
   */
  public User getUser();

  /**
   * Add a command to the queue to allow users to undo or redo changes they make
   * 
   * @param command Command to add at the front of the list
   */
  public void addCommand(PCCommand command);

  /**
   * Instruct the end-user how to use the application
   */
  public void instructions();

  /**
   * Initialize the classes handling of commands
   */
  public void initCommands();

  /**
   * Decides if the user can undo a command
   */
  public Boolean canUndo();

  /**
   * Decides if the user can redo a command
   */
  public Boolean canRedo();

  /**
   * Undo the most recent command
   */
  public String undo();

  /**
   * Redo the most recent undo
   */
  public String redo();

  /**
   * Undo all changes made within the session
   */
  public void undoAll();

  /**
   * Redo all undos made within the session
   */
  public void redoAll();

}
