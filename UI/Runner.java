package UI;

import java.util.Scanner;

import UI.Concrete.CLIMediator;

public class Runner {

  public static void main(String[] args) {
    try {
      Scanner scanner = new Scanner(System.in);
      CLIMediator mediator = new CLIMediator();
      System.out.println("Welcome to Comix, Press \"I\" to view the instructions");
      mediator.mediate(scanner);
    } catch (Exception err) {
      System.out.println("Scanner Error: ");
      System.out.println(err.getLocalizedMessage() + "");
      System.out.println(err.getMessage() + "");
    }
  }
}
