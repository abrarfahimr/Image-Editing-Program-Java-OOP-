package controller.commands;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import model.IImage;
import model.IImageStorage;
import view.writer.IWriter;
import view.writer.PPMWriter;
import view.writer.StandardImageWriter;

/**
 * Command to save an image to a file.
 */
public class SaveCommand implements ICommand {
  private final String filePath;
  private final String destImageName;
  private final IImageStorage imageDatabase;

  /**
   * Constructor initializes the file path, image name, and image database to write to a file.
   *
   * @param filePath      of type String, the file path where the image should be saved.
   * @param destImageName     of type String. Name of the image to save.
   * @param imageDatabase of type IImageStorage. Hashmap where images are stored.
   */
  public SaveCommand(String filePath, String destImageName, IImageStorage imageDatabase) {
    this.filePath = filePath;
    this.destImageName = destImageName;
    this.imageDatabase = imageDatabase;
  }

  /**
   * Executes the image manipulation operation.
   *
   * @throws IllegalArgumentException if command is null or not found.
   */
  @Override
  public void run() throws IllegalArgumentException {
    try {
      IImage image = imageDatabase.getImage(destImageName);
      IWriter writer;
      String fileType = getFileExtension(filePath);

      switch (fileType.toLowerCase()) {
        case "ppm":
          writer = new PPMWriter(new FileWriter(filePath));
          break;
        case "jpg":
        case "jpeg":
          writer = new StandardImageWriter(new FileOutputStream(filePath), "jpg");
          break;
        case "png":
          writer = new StandardImageWriter(new FileOutputStream(filePath), "png");
          break;
        case "bmp":
          writer = new StandardImageWriter(new FileOutputStream(filePath), "bmp");
          break;
        default:
          throw new IllegalArgumentException("Wrong file format: " + fileType);
      }

      writer.write(image);

    } catch (IOException e) {
      throw new IllegalStateException("Failed to save image: " + e.getMessage());
    }
  }

  /**
   * Helper class to get the file type name.
   *
   * @param filePath of type String, the file path where the image is located.
   * @return a type String of the file type.
   */
  private String getFileExtension(String filePath) {
    int lastDot = filePath.lastIndexOf('.');
    if (lastDot == -1 || lastDot == filePath.length() - 1) {
      return "";
    }
    // gets all characters after the ".", e.g. png
    return filePath.substring(lastDot + 1);
  }
}
