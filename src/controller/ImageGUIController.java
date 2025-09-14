package controller;

import java.awt.image.BufferedImage;

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
import model.IImage;
import model.IImageHistogram;
import model.IImageStorage;
import model.ImageHistogramImpl;
import view.GUIView;
import view.ImageViewListener;
import view.writer.StandardImageWriter;

/**
 * Controller for the GUI view of the program.
 * Implements the ImageViewListener and IImageController interface
 * to handle high-level view events and trigger the correct commands.
 */
public class ImageGUIController implements ImageViewListener, IImageController {
  private final GUIView view;
  private final IImageStorage imageStorage;

  /**
   * Constructor for the GUI controller.
   *
   * @param view         the GUI view.
   * @param imageStorage the image storage model.
   */
  public ImageGUIController(GUIView view, IImageStorage imageStorage) {
    this.view = view;
    this.imageStorage = imageStorage;

    // Add Event Listeners
    view.addViewListener(this);
  }

  /**
   * Method executes the image manipulate program.
   *
   * @throws IllegalStateException if fail to read or write from or to inputs.
   */
  @Override
  public void run() throws IllegalStateException {
    // Set the visibility to true
    view.setVisible(true);
  }

  /**
   * Called when the user requests to load an image.
   *
   * @param filePath  the path to the image file.
   * @param imageName the name to assign to the loaded image.
   */
  @Override
  public void onLoadImage(String filePath, String imageName) {
    try {
      // Load the image with the path
      ICommand loadCommand = new LoadCommand(filePath, imageName, imageStorage);

      runAndDisplayImage(loadCommand, imageName, imageStorage, view);

      view.showMessage("Image loaded successfully: " + imageName);

    } catch (Exception e) {
      view.showError("Failed to load image: " + e.getMessage());
    }

  }

  /**
   * Called when the user requests to save an image.
   *
   * @param filePath  the path where the image should be saved.
   * @param imageName the name to assign to the saved image.
   */
  @Override
  public void onSaveImage(String filePath, String imageName) {
    try {
      // Create and execute save command
      ICommand saveCommand = new SaveCommand(filePath, imageName, imageStorage);
      saveCommand.run();

      view.showMessage("Image saved successfully to: " + filePath);

    } catch (Exception e) {
      view.showError("Failed to save image: " + e.getMessage());
    }
  }

  /**
   * Called when the user requests to delete an image.
   *
   * @param imageName the name of the image to delete.
   */
  @Override
  public void onDeleteImage(String imageId, String imageName) {
    try {
      // Remove image from storage
      imageStorage.removeImage(imageId);

      // Remove from view list
      view.removeImageName(imageId);

      // Clear display if this was the displayed image
      view.clearImageDisplay();

      view.showMessage("Image deleted successfully: " + imageId);

    } catch (Exception e) {
      view.showError("Failed to delete image: " + e.getMessage());
    }

  }

  /**
   * Called when the user requests to brighten an image.
   *
   * @param imageName the name of the image to brighten.
   * @param amount    the amount to brighten (can be negative to darken).
   */
  @Override
  public void onBrightnessOperation(String imageName, int amount) {
    try {
      // Create destination name
      String destName = imageName + "-bright" + amount;

      // Create and execute brighten command. Command adds new image to storage
      ICommand brightenCommand = new BrightenCommand(amount, imageName, destName, imageStorage);

      runAndDisplayImage(brightenCommand, destName, imageStorage, view);

      view.showMessage("Brightness adjusted by " + amount);

    } catch (Exception e) {
      view.showError("Failed to adjust brightness: " + e.getMessage());
    }
  }

  /**
   * Called when the user requests a grayscale operation.
   *
   * @param imageName the name of the source image.
   * @param operation the type of grayscale operation.
   *                  (e.g., "red-component", "intensity-component").
   */
  @Override
  public void onGrayscaleOperation(String imageName, String operation) {
    try {
      // Create destination name
      String destName = imageName + "-" + operation.replace("-", "");

      // Create and execute the appropriate command
      ICommand command = createGrayscaleCommand(operation, imageName, destName);

      runAndDisplayImage(command, destName, imageStorage, view);

    } catch (Exception e) {
      view.showError("Failed to apply " + operation + ": " + e.getMessage());
    }
  }

  /**
   * Called when the user selects an image from already loaded images.
   *
   * @param imageName the name of the image to display.
   */
  @Override
  public void displaySelectedImage(String imageName) {
    try {
      // Get the selected image from storage
      IImage selectedImage = imageStorage.getImage(imageName);

      // Convert to BufferedImage and display
      BufferedImage bufferedImage = convertToBufferedImage(selectedImage);
      view.updateViewingArea(bufferedImage);

      // Generate and update histogram
      IImageHistogram histogram = generateHistogram(selectedImage);
      view.updateHistogram(histogram);

    } catch (Exception e) {
      view.showError("Failed to display selected image: " + e.getMessage());
    }
  }

  /**
   * Helper function to save, load image to view, and generate the histogram.
   *
   * @param command      the command to execute.
   * @param imageName    name of the image.
   * @param imageStorage THe image database.
   * @param view         GUI view to display the image in.
   */
  private void runAndDisplayImage(
          ICommand command, String imageName, IImageStorage imageStorage, GUIView view) {
    command.run();

    // Add the imageId to the view
    view.addImageId(imageName);

    // Display to view
    IImage loadedImage = imageStorage.getImage(imageName);

    // Convert IImage to a buffered image
    BufferedImage bufferedImage = convertToBufferedImage(loadedImage);
    view.updateViewingArea(bufferedImage);

    // Generate and update histogram
    IImageHistogram histogram = generateHistogram(loadedImage);
    view.updateHistogram(histogram);
  }

  /**
   * Create the appropriate grayscale command based on the operation type.
   *
   * @param operation  the grayscale operation type.
   * @param sourceName the source image name.
   * @param destName   the destination image name.
   * @return the command to execute.
   */
  private ICommand createGrayscaleCommand(String operation, String sourceName, String destName) {
    switch (operation) {
      case "red-component":
        return new RedGreyScaleCommand(sourceName, destName, imageStorage);
      case "green-component":
        return new GreenGreyScaleCommand(sourceName, destName, imageStorage);
      case "blue-component":
        return new BlueGreyScaleCommand(sourceName, destName, imageStorage);
      case "value-component":
        return new ValueGreyScaleCommand(sourceName, destName, imageStorage);
      case "intensity-component":
        return new IntensityGreyScaleCommand(sourceName, destName, imageStorage);
      case "luma-component":
        return new LumaGreyScaleCommand(sourceName, destName, imageStorage);
      default:
        throw new IllegalArgumentException("Unknown grayscale operation: " + operation);
    }
  }

  /**
   * Convert IImage to BufferedImage for display in the view.
   *
   * @param image the IImage to convert.
   * @return BufferedImage for Swing display.
   */
  private BufferedImage convertToBufferedImage(IImage image) {
    return StandardImageWriter.convertToBufferedImage(image);
  }

  /**
   * Generate histogram data for the given image using the factory.
   *
   * @param image the image to generate histogram for.
   * @return the histogram data, or null if generation fails.
   */
  private IImageHistogram generateHistogram(IImage image) {
    try {
      return new ImageHistogramImpl(image);
    } catch (Exception e) {
      // If histogram generation fails, return null
      // View handles this error and displays "no histogram data"
      System.err.println("Failed to generate histogram: " + e.getMessage());
      return null;
    }
  }
}
