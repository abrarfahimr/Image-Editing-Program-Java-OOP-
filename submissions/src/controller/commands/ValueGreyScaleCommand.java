package controller.commands;

import model.IImageStorage;
import operations.IOperation;
import operations.ValueGreyScaleOperation;

/**
 * Command class for value greyscale operation that extends AbstractGreyScale.
 */
public class ValueGreyScaleCommand extends AbstractGreyScaleCommand {
  /**
   * Constructor initializes the variables to greyscale an image.
   *
   * @param sourceName    of type string, source name of the image.
   * @param destName      of type string, destination name of the image.
   * @param imageDatabase of type IImageStorage. Hashmap where images are stored.
   */
  public ValueGreyScaleCommand(String sourceName, String destName, IImageStorage imageDatabase) {
    super(sourceName, destName, imageDatabase);
  }

  /**
   * Get the specific IOperation class to apply on the image.
   *
   * @return the IOperation class to apply on the image.
   */
  @Override
  protected IOperation getGreyScaleOperation() {
    return new ValueGreyScaleOperation();
  }
}
