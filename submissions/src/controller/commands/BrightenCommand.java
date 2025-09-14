package controller.commands;

import model.IImage;
import model.IImageStorage;
import operations.BrightenOperation;

/**
 * Command class to brighten an image.
 */
public class BrightenCommand implements ICommand {
  private final int value;
  private final String sourceName;
  private final String destName;
  private final IImageStorage imageDatabase;

  /**
   * Constructor initializes the variables to brighten an image.
   *
   * @param value         of type int, the amount to brighten the image.
   * @param sourceName    of type string, source name of the image.
   * @param destName      of type string, destination name of the image.
   * @param imageDatabase of type IImageStorage. Hashmap where images are stored.
   */
  public BrightenCommand(
          int value, String sourceName, String destName, IImageStorage imageDatabase) {
    this.value = value;
    this.sourceName = sourceName;
    this.destName = destName;
    this.imageDatabase = imageDatabase;
  }

  /**
   * Executes the image manipulation operation.
   *
   * @throws IllegalArgumentException if command is null or not found.
   */
  @Override
  public void run() throws IllegalArgumentException {
    IImage sourceImage = imageDatabase.getImage(sourceName);
    IImage result = new BrightenOperation(value).apply(sourceImage);
    imageDatabase.putImage(destName, result);
  }
}
