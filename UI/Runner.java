package UI;

import java.util.Scanner;

import UI.Concrete.CLIMediator;

public class Runner {

  public static void main(String[] args) {
    try {
      Scanner scanner = new Scanner(System.in);
      CLIMediator mediator = new CLIMediator();
      mediator.mediate(scanner);
    } catch (Exception err) {
      System.out.println("Scanner Error: ");
      System.out.println(err.getLocalizedMessage() + "");
      System.out.println(err.getMessage() + "");
    }
  }
}
