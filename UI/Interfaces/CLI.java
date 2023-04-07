package UI.Interfaces;

import java.util.Scanner;

public interface CLI {

  public void instructions(Boolean includeLast);

  public String readInput(Scanner scanner);

  public void displayMessage(String message);

}
