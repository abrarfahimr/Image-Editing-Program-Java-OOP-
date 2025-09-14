import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.IImageController;
import controller.ImageControllerImpl;
import controller.ImageGUIController;
import model.IImageStorage;
import model.ImageStorageImpl;
import view.GUIView;

/**
 * Main entry point for the image processing application.
 * Supports both running the program using a script file or using the console.
 */
public class ProcessImage {
  /**
   * Main method to run the application.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    IImageStorage imageStorage = new ImageStorageImpl();

    try {
      if (args.length == 0) {
        // If there is no argument, run in GUI Mode
        runGUIMode(imageStorage);
      } else if (args.length == 1) {
        if (args[0].equals("-text")) {
          // Text mode (Type in console)
          runInteractive(imageStorage);
        } else {
          // Single argument - Script mode
          processScriptFile(args[0], imageStorage);
        }
      } else if (args.length == 2 && args[0].equals("-file")) {
        // -file followed by script path
        processScriptFile(args[1], imageStorage);
      } else {
        // Invalid arguments, show usage information and exit
        showUsageAndExit();
      }
    } catch (Exception e) {
      System.err.println("Unexpected error: " + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Run the application in GUI mode.
   */
  private static void runGUIMode(IImageStorage imageStorage) {
    try {
      javax.swing.UIManager.setLookAndFeel(
              javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      // Use Default
    }

    // Create view and controller
    GUIView view = new GUIView();
    IImageController controller = new ImageGUIController(view, imageStorage);
    controller.run();
  }

  /**
   * Process commands from a script file.
   * Usage: Create a text file with all the commands, from the console write one of these commands
   * java ProcessImage mycommands.txt
   * OR
   * java ProcessImage -file mycommands.txt
   */
  private static void processScriptFile(String filename, IImageStorage imageStorage) {
    try (FileReader reader = new FileReader(filename)) {
      IImageController controller = new ImageControllerImpl(reader, System.out, imageStorage);
      controller.run();
      System.out.println("Script processing completed.");
    } catch (FileNotFoundException e) {
      System.err.println("Error: Script file not found: " + filename);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Error reading script file: " + e.getMessage());
      System.exit(1);
    } catch (Exception e) {
      System.err.println("Error during script execution: " + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Run the program in the console.
   * Write quit, exit, or q to close the program.
   */
  private static void runInteractive(IImageStorage imageStorage) {
    System.out.println("Welcome to Image Manipulation and Enhancement App");
    System.out.println("---------------------------------------------------");
    System.out.println("Here are the Commands:");
    System.out.println(
            "load, save, brighten, red-component, green-component, " +
                    "blue-component, value-component, intensity-component, luma-component");
    System.out.println("---------------------------------------------------");
    System.out.println("Type 'quit' or 'exit' to close the program");
    System.out.println("What image do you want to edit?");

    IImageController controller = new ImageControllerImpl(
            new InputStreamReader(System.in),
            System.out,
            imageStorage
    );

    try {
      controller.run();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  /**
   * Show usage information and exit.
   */
  private static void showUsageAndExit() {
    System.err.println("Invalid command line arguments.");
    System.err.println("Usage:");
    System.err.println("  java Main                    - Run in GUI mode");
    System.err.println("  java Main -text              - Run in interactive text mode");
    System.err.println("  java Main -file <script>     - Execute script file");
    System.err.println("  java Main <script>           - Execute script file");
    System.exit(1);
  }
}
