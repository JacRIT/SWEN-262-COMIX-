package UI.Concrete;

import Api.GuestComixAPI;
import Model.JavaObjects.Comic;
import UI.Interfaces.Mediator;

public class ComicInterpreter extends AuthInterpreter {

  private Comic comic;

  public ComicInterpreter(Mediator mediator, GuestComixAPI api) {
    this(mediator, api, new Comic());
  }

  public ComicInterpreter(Mediator mediator, GuestComixAPI api, Comic comic) {
    super(mediator, api);
    this.comic = comic;
  }

  @Override
  public String interprete(String input) {

    String command = input;

    if (command.equals("I") || command.equals("i")) {
      return this.printComicInstructions();
    }

    if (command.equals("Back") || command.equals("back")) {
      switchToAuth();
      return "Comic abandoned without saving";
    }

    String[] splitCommand = command.split("=");
    String value = null;

    if (splitCommand.length > 1) {
      command = splitCommand[0];
      value = splitCommand[1];
    }

    String noNumberFound = "Field \"" + command + "\" requires a number be passed in";
    String noBooleanFound = "Field \"" + command + "\" requires \"True/False\" be passed in";
    String missingMessage = "Missing a value for field \"" + command + "\"";
    String successMessage = "Comic \"" + command + "\" set to \"" + value
        + "\"\nEnter \"I\" to view instructions and current status of your Comic";

    if (this.validComic())
      successMessage += "\nEnter \"Create\" when you're ready to add comic into the database";

    if (command.equals("Title") || command.equals("title")) {
      if (value == null)
        return missingMessage;

      comic.setTitle(value);
      return successMessage;
    }

    if (command.equals("Series") || command.equals("series")) {
      if (value == null)
        return missingMessage;

      comic.setSeries(value);
      return successMessage;
    }

    if (command.equals("IssueNumber") || command.equals("issueNumber")) {
      if (value == null)
        return missingMessage;

      comic.setIssueNumber(value);
      return successMessage;
    }

    if (command.equals("PublicationDate") || command.equals("publicationDate")) {
      if (value == null)
        return missingMessage;

      comic.setPublicationDate(value);
      return successMessage;
    }

    if (command.equals("Description") || command.equals("description")) {
      if (value == null)
        return missingMessage;

      comic.setDescription(value);
      return successMessage;
    }

    if (command.equals("IsSlabbed") || command.equals("isSlabbed")) {
      if (value == null)
        return missingMessage;

      Boolean isSlabbed = null;

      if (value.equals("True") || value.equals("true"))
        isSlabbed = true;

      if (value.equals("False") || value.equals("false"))
        isSlabbed = false;

      if (isSlabbed == null)
        return noBooleanFound;

      comic.setSlabbed(isSlabbed);
      return successMessage;
    }

    // Comic needs to be updated to allow adding/removing singular fields to array

    // if (command.equals("Publisher") || command.equals("publisher")) {
    // if (value == null)
    // return missingMessage;

    // comic.addPublisher(value);
    // return successMessage;
    // }

    // if (command.equals("Creators") || command.equals("creators")) {
    // if (value == null)
    // return missingMessage;

    // comic.addCreator(value);
    // return successMessage;
    // }

    // if (command.equals("PrinciplCharacters") ||
    // command.equals("principlCharacters")) {
    // if (value == null)
    // return missingMessage;

    // comic.addPrinciplCharacter(value);
    // return successMessage;
    // }

    // All Numbers

    try {

      int integerValue = -1;
      Float floatValue = (float) -1;

      if (command.equals("VolumeNumber") || command.equals("volumeNumber")) {
        if (value == null)
          return missingMessage;

        integerValue = Integer.parseInt(value);

        comic.setVolumeNumber(integerValue);
        return successMessage;
      }

      if (command.equals("InitialValue") || command.equals("initialValue")) {
        if (value == null)
          return missingMessage;

        floatValue = Float.parseFloat(value);

        comic.setInitialValue(floatValue);
        return successMessage;
      }

      if (command.equals("Grade") || command.equals("grade")) {
        if (value == null)
          return missingMessage;

        integerValue = Integer.parseInt(value);

        if (integerValue < 0 || integerValue > 10)
          return noNumberFound + " the range of 1-10";

        comic.setGrade(integerValue);
        return successMessage;
      }

    } catch (Exception err) {
      return noNumberFound;
    }

    return "Unrecognized Command: " + command + "\nPlease Try Again";
  }

  private String printComicInstructions() {
    String instructions = "Current Comic:\n" + this.comic.toStringDetailed()
        + "\nEnter \"I\" to view instructions and current status of your Comic"
        + "\nEnter a \"<key>=<value>\" to set a field in the comic"
        + "\nEnter a \"Back\" to abandon creation";

    if (this.validComic())
      instructions += "\nEnter \"Create\" when you're ready to add comic into the database";

    return instructions;
  }

  private void switchToAuth() {
    this.mediator.setCli(new AuthCLI(this.mediator));
    this.mediator.setInterpreter(new AuthInterpreter(this.mediator, this.api));
  }

  private Boolean validComic() {
    if (comic.getTitle().length() == 0)
      return false;

    if (comic.getDescription().length() == 0)
      return false;

    if (comic.getPublicationDate().length() == 0)
      return false;

    if (comic.getIssueNumber().length() == 0)
      return false;

    if (comic.getInitialValue() == 0)
      return false;

    if (comic.getVolumeNumber() == 0)
      return false;

    return true;
  }

}
