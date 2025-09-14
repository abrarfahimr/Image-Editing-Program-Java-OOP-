package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import controller.commands.BlueGreyScaleCommand;
import controller.commands.BrightenCommand;
import controller.commands.GreenGreyScaleCommand;
import controller.commands.ICommand;
import controller.commands.IntensityGreyScaleCommand;
import controller.commands.LoadCommand;
import controller.commands.LumaGreyScaleCommand;
import controller.commands.RedGreyScaleCommand;
import controller.commands.SaveCommand;
import controller.commands.ValueGreyScaleCommand;
import model.IImageStorage;

/**
 * Controller to process the image.
 * It uses a command pattern to load, edit, and save the image.
 * A protected hashmap is used to store the command.
 * It checks the inputs for errors and provide the correct message for it.
 */
public class ImageControllerImpl implements IImageController {
  private final Scanner scanner;
  private final Appendable appendable;
  private final IImageStorage imageDatabase;
  protected final Map<String, Function<Scanner, ICommand>> knownCommands;

  /**
   * Initialize a new controller with the given input, output, and database.
   *
   * @param input         of type Readable is the source of commands.
   * @param appendable    of type Appendable. Provides an output to write the new image.
   * @param imageDatabase the image storage of type IImageStorage.
   */
  public ImageControllerImpl(Readable input, Appendable appendable, IImageStorage imageDatabase) {
    this.scanner = new Scanner(input);
    this.appendable = appendable;
    this.imageDatabase = imageDatabase;
    this.knownCommands = new HashMap<>();
    initializeCommands();
  }

  /**
   * Initialize the map with commands.
   */
  private void initializeCommands() {
    // Load Command
    knownCommands.put("load", (Scanner s) -> {
      try {
        String filePath = s.next();
        String imageName = s.next();
        return new LoadCommand(filePath, imageName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException("Wrong inputs. Usage: load image-path image-name");
      }
    });
    // Save Command
    knownCommands.put("save", (Scanner s) -> {
      try {
        String filePath = s.next();
        String imageName = s.next();
        return new SaveCommand(filePath, imageName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException("Wrong inputs. Usage: save image-path image-name");
      }
    });
    // Brighten Command
    knownCommands.put("brighten", (Scanner s) -> {
      try {
        int increment = s.nextInt();
        String filePath = s.next();
        String imageName = s.next();
        return new BrightenCommand(increment, filePath, imageName, imageDatabase);
      } catch (InputMismatchException e) {
        throw new IllegalArgumentException("increment must be an integer");
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: brighten increment image-name dest-image-name");
      }
    });
    // Red component Command
    knownCommands.put("red-component", (Scanner s) -> {
      try {
        String sourceName = s.next();
        String destName = s.next();
        return new RedGreyScaleCommand(sourceName, destName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: red-component image-name dest-image-name");
      }
    });
    // Green component Command
    knownCommands.put("green-component", (Scanner s) -> {
      try {
        String sourceName = s.next();
        String destName = s.next();
        return new GreenGreyScaleCommand(sourceName, destName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: green-component image-name dest-image-name");
      }
    });
    // Blue component Command
    knownCommands.put("blue-component", (Scanner s) -> {
      try {
        String sourceName = s.next();
        String destName = s.next();
        return new BlueGreyScaleCommand(sourceName, destName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: blue-component image-name dest-image-name");
      }
    });
    // Value component Command
    knownCommands.put("value-component", (Scanner s) -> {
      try {
        String sourceName = s.next();
        String destName = s.next();
        return new ValueGreyScaleCommand(sourceName, destName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: value-component image-name dest-image-name");
      }
    });
    // Intensity component Command
    knownCommands.put("intensity-component", (Scanner s) -> {
      try {
        String sourceName = s.next();
        String destName = s.next();
        return new IntensityGreyScaleCommand(sourceName, destName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: intensity-component image-name dest-image-name");
      }
    });
    // Luma component Command
    knownCommands.put("luma-component", (Scanner s) -> {
      try {
        String sourceName = s.next();
        String destName = s.next();
        return new LumaGreyScaleCommand(sourceName, destName, imageDatabase);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
                "Wrong inputs. Usage: luma-component image-name dest-image-name");
      }
    });
  }

  /**
   * Private helper function to write the error message to the appendable.
   *
   * @param message of type String showing what the error is.
   */
  private void writeMessage(String message) {
    try {
      appendable.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output: e");
    }
  }

  /**
   * Method executes the image manipulate program.
   *
   * @throws IllegalStateException if fail to read or write from or to inputs.
   */
  @Override
  public void run() throws IllegalStateException {
    while (scanner.hasNext()) {
      String commandName = scanner.next();

      // Check for quit/exit commands
      if (commandName.equalsIgnoreCase("quit") ||
              commandName.equalsIgnoreCase("exit") ||
              commandName.equalsIgnoreCase("q")) {
        writeMessage("Exiting program...\n");
        return;
      }

      // if it is a comment skip
      if (commandName.startsWith("#")) {
        scanner.nextLine(); // Skip the line
        continue;
      }

      // if commandName is empty skip
      if (commandName.isEmpty()) {
        continue;
      }

      Function<Scanner, ICommand> cmdFunction = knownCommands.get(commandName.toLowerCase());

      // if command is null, Show the error message
      if (cmdFunction == null) {
        writeMessage("Error: Unknown command '" + commandName + "'\n");
        scanner.nextLine(); // Skip rest of line
        continue;
      }

      try {
        // Create and execute command
        ICommand command = cmdFunction.apply(scanner);
        command.run();

      } catch (IllegalArgumentException | IllegalStateException e) {
        writeMessage("Error: " + e.getMessage() + "\n");
      } catch (Exception e) {
        writeMessage("Error executing " + commandName + ": " + e.getMessage() + "\n");
      }

      // clear the rest of the tokens
      if (scanner.hasNextLine()) {
        scanner.nextLine();
      }
    }
  }
}
