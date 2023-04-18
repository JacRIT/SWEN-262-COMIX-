package UI.Concrete;

import Api.GuestComixAPI;
import Model.Command.PCCommand;
import Model.Command.ConcreteCommand.CreateComic;
import Model.JavaObjects.Character;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Creator;
import Model.JavaObjects.Signature;
import UI.Interfaces.Mediator;

public class ComicInterpreter extends AuthInterpreter {

  private Comic comic;

  public ComicInterpreter(Mediator mediator, GuestComixAPI api) {
    this(mediator, api, new Comic());
  }

  public ComicInterpreter(Mediator mediator, GuestComixAPI api, Comic comic) {
    super(mediator, api);
    this.comic = comic;
    this.comic.setValue(0);
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
      successMessage += "\nEnter \"Submit\" when you're ready to add comic into the database";

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

    if (command.startsWith("Add") || command.startsWith("add")) {
      String[] addSplit = command.split(" ");

      if (addSplit.length <= 1)
        return "Missing key for \"Add\" command";

      if (value == null)
        return "Missing value for \"Add\" command";

      String key = addSplit[1];

      successMessage = this.addToComicCommand(key, value);

      return successMessage;

    }

    if (command.startsWith("Remove") || command.startsWith("remove")) {
      String[] removeSplit = command.split(" ");

      if (removeSplit.length <= 1)
        return "Missing key for \"Remove\" command";

      if (value == null)
        return "Missing value for \"Remove\" command";

      String key = removeSplit[1];

      successMessage = this.removeFromComicCommand(key, value);

      return successMessage;

    }

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

      if (command.equals("PublicationDate") || command.equals("publicationDate")) {
        if (value == null)
          return missingMessage;

        String[] dateSplit = value.split("/");

        if (dateSplit.length != 3 || dateSplit[2].length() != 4)
          return "Invalid input: " + value + "\nPublicationDate must be provided in the following format: mm/dd/yyyy";

        int month = Integer.parseInt(dateSplit[0]);
        int day = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);

        if (month > 12 || month < 0)
          return "Invalid input: " + value + "\nPublicationDate must be provided in the following format: mm/dd/yyyy";
        if (day > 31 || day < 0)
          return "Invalid input: " + value + "\nPublicationDate must be provided in the following format: mm/dd/yyyy";
        if (year < 0)
          return "Invalid input: " + value + "\nPublicationDate must be provided in the following format: mm/dd/yyyy";

        comic.setReleaseMonth(month);
        comic.setReleaseDay(day);
        comic.setReleaseYear(year);

        return successMessage;
      }

    } catch (Exception err) {
      return noNumberFound;
    }

    try {
      if (command.equals("Submit") || command.equals("submit")) {
        if (!this.validComic())
          return this.missingFields();

        PCCommand pcCommand = new CreateComic(this.mediator.getUser(), this.comic, this.api);

        successMessage = pcCommand.execute();
        this.mediator.addCommand(pcCommand);
        this.switchToAuth();
        return successMessage;
      }
    } catch (Exception err) {
      System.out.println("Error:\n" + err.getMessage() + "\n" + err.getLocalizedMessage());
      err.printStackTrace();
      return "Comic could not be created";
    }

    return "Unrecognized Command: " + command + "\nPlease Try Again";
  }

  private String printComicInstructions() {
    String instructions = "Current Comic:\n" + this.comic.toStringDetailed()
        + "\nEnter \"I\" to view instructions and current status of your Comic"
        + "\nEnter a \"<key>=<value>\" to set a field in the comic"
        + "\nEnter a \"Add <key>=<value>\" to add a field into the\n\tcreators/principlCharacters/signatures collections on your comic"
        + "\nEnter a \"Remove <key>=<value>\" to remove a field from the\n\tcreators/principlCharacters/signatures collections on your comic"
        + "\nEnter a \"Back\" to abandon creation";

    String invalidFields = "\n\n" + this.missingFields();

    if (this.validComic())
      instructions += "\n\nEnter \"Submit\" when you're ready to add comic into the database";
    else
      instructions += invalidFields;

    return instructions;
  }

  private String addToComicCommand(String key, String value) {
    String ending = "\"\nEnter \"I\" to view instructions and current status of your Comic";
    if (key.equals("Creators") || key.equals("creators")) {
      this.comic.addCreator(new Creator(0, value));
      return value + " added to comic's " + key + ending;
    }

    if (key.equals("PrinciplCharacters") || key.equals("principlCharacters")) {
      this.comic.addPrinciplCharacter(new Character(0, value));
      return value + " added to comic's " + key + ending;
    }

    if (key.equals("Signatures") || key.equals("signatures")) {
      this.comic.addSignature(new Signature(0, value));
      return value + " added to comic's " + key + ending;
    }

    return "Unrecognized Key: " + key + "\nPlease Try Again";
  }

  private String removeFromComicCommand(String key, String value) {
    String ending = "\"\nEnter \"I\" to view instructions and current status of your Comic";
    if (key.equals("Creators") || key.equals("creators")) {
      this.comic.removeCreator(new Creator(0, value));
      return value + " removed from comic's " + key + ending;
    }

    if (key.equals("PrinciplCharacters") || key.equals("principlCharacters")) {
      this.comic.removePrinciplCharacter(new Character(0, value));
      return value + " removed from comic's " + key + ending;
    }

    if (key.equals("Signatures") || key.equals("signatures")) {
      this.comic.removeSignature(new Signature(0, value));
      return value + " removed from comic's " + key + ending;
    }

    return "Unrecognized Key: " + key + "\nPlease Try Again";
  }

  private void switchToAuth() {
    this.mediator.setCli(new AuthCLI(this.mediator));
    this.mediator.setInterpreter(new AuthInterpreter(this.mediator, this.api));
  }

  private Boolean validComic() {

    /**
     * Title
     * Series
     * Issue Number
     * PublicationDate xx/xx/xxxx before submit tear apart and set release
     * day/month/year
     */

    if (comic.getTitle().length() == 0)
      return false;

    if (comic.getSeries().length() == 0)
      return false;

    if (comic.getIssueNumber().length() == 0)
      return false;

    if (comic.getReleaseDay() == 0 || comic.getReleaseMonth() == 0 || comic.getReleaseYear() == 0)
      return false;

    return true;
  }

  private String missingFields() {
    String message = "To create a comic the following fields must not be empty:";

    if (comic.getTitle().length() == 0)
      message += "\ntitle,";

    if (comic.getSeries().length() == 0)
      message += "\nseries,";

    if (comic.getIssueNumber().length() == 0)
      message += "\nissueNumber,";

    if (comic.getReleaseDay() == 0 || comic.getReleaseMonth() == 0 || comic.getReleaseYear() == 0)
      message += "\npublicationDate: mm/dd/yyyy,";

    return message.substring(0, message.length() - 1);
  }
}
