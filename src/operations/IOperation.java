package operations;

import model.IImage;

/**
 * Interface for operations that the image editor can use.
 * Operations include Brighten, Darken, Greyscale based on the red, green, and blue color.
 * Greyscale based on the Intensity, Value and Luma of the image.
 * Every operation has only 1 method, apply() that take in an Object of type IImage
 * and returns a new Object also of type IImage.
 */
public interface IOperation {
  /**
   * Apply the respective operation by changing the rgb values of each pixel in the image.
   * @param image object of type IImage.
   * @return a new object of type IImage after the changes are applied.
   * @throws IllegalArgumentException if image object is null.
   */
  IImage apply(IImage image) throws IllegalArgumentException;
}
