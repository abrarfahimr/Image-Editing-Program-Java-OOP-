package controller.commands;

import model.IImage;
import model.IImageStorage;
import operations.IOperation;

/**
 * Abstract class for all greyscale commands.
 * It contains the logic similar to all greyscale operations
 * but only abstracts the operation that is used to perform the image manipulation.
 */
public abstract class AbstractGreyScaleCommand implements ICommand {
  protected final String sourceName;
  protected final String destName;
  protected final IImageStorage imageDatabase;

  /**
   * Constructor initializes the variables to greyscale an image.
   *
   * @param sourceName    of type string, source name of the image.
   * @param destName      of type string, destination name of the image.
   * @param imageDatabase of type IImageStorage. Hashmap where images are stored.
   */
  public AbstractGreyScaleCommand(String sourceName, String destName, IImageStorage imageDatabase) {
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
    IOperation greyscaleOperation = getGreyScaleOperation();
    IImage result = greyscaleOperation.apply(sourceImage);

    imageDatabase.putImage(destName, result);
  }

  /**
   * Get the specific IOperation class to apply on the image.
   *
   * @return the IOperation class to apply on the image.
   */
  protected abstract IOperation getGreyScaleOperation();
}
