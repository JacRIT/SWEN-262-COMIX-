package UI.Concrete;

public class GuestCLI extends DefaultCLI {

  public GuestCLI() {

  }

  @Override
  public void instructions(Boolean includeLast) {
    super.instructions(false);

    this.log("R <Username> - Register a new account with the provided username");
    this.log("L <Username> - Log into your account with the provided username");

    if (includeLast)
      this.log("Exit - Exit Comix");

  }

}
