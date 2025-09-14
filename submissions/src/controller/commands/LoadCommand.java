package controller.commands;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import model.IImage;
import model.IImageStorage;
import view.reader.IViewReader;
import view.reader.PPMReader;
import view.reader.StandardImageReader;

/**
 * Class to load the image and create the IImage object and store them in a hashmap.
 */
public class LoadCommand implements ICommand {
  private final String filePath;
  private final String srcImageName;
  private final IImageStorage imageDatabase;

  /**
   * Constructor takes in the file path, image name and database to create the IImage object
   * of the corresponding image and store the name and object into the hashmap.
   *
   * @param filePath      of type String, the file path where the image is located.
   * @param srcImageName     of type String. Name of the source image.
   * @param imageDatabase of type IImageStorage. Hashmap where images are stored.
   */
  public LoadCommand(String filePath, String srcImageName, IImageStorage imageDatabase) {
    this.filePath = filePath;
    this.srcImageName = srcImageName;
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
      IViewReader reader;
      String extension = getFileExtension(filePath);

      switch (extension.toLowerCase()) {
        case "ppm":
          reader = new PPMReader(new FileReader(filePath));
          break;
        case "jpg":
        case "jpeg":
          reader = new StandardImageReader(new FileInputStream(filePath), "jpg");
          break;
        case "png":
          reader = new StandardImageReader(new FileInputStream(filePath), "png");
          break;
        case "bmp":
          reader = new StandardImageReader(new FileInputStream(filePath), "bmp");
          break;
        default:
          throw new IllegalArgumentException("Unsupported file format: " + extension);
      }

      IImage image = reader.read();
      imageDatabase.putImage(srcImageName, image);

    } catch (IOException e) {
      throw new IllegalStateException("Failed to load image: " + e.getMessage());
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

