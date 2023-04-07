package UI.Interfaces;

import java.util.Scanner;

public interface CLI {

  /**
   * Display the instructions for using the specific CLI
   * 
   * @param includeLast Boolean that indicates if the exit command should be
   *                    displayed as well
   */
  public void instructions(Boolean includeLast);

  /**
   * Take a users input from the command line
   * 
   * @param scanner The Scanner object required to read from the command line
   * @return The input entered in by the user
   */
  public String readInput(Scanner scanner);

  /**
   * Display a specified message in the CLI's format
   * 
   * @param message The specified message to display
   */
  public void displayMessage(String message);

}
