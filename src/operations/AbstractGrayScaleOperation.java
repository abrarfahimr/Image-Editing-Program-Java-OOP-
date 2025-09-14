package operations;

import model.IImage;
import model.ImageImpl;

/**
 * Abstract class that implements IOperation interface.
 * It is an abstract class that all operations extends from.
 * It implements the apply() method and let the respective operation class implement
 * the grey value to use for the new IImage Object.
 */
public abstract class AbstractGrayScaleOperation implements IOperation {
  /**
   * Apply the respective operation by changing the rgb values of each pixel in the image.
   *
   * @param image object of type IImage.
   * @return a new object of type IImage after the changes are applied.
   * @throws IllegalArgumentException if image object is null.
   */
  @Override
  public IImage apply(IImage image) {
    if (image == null) {
      throw new IllegalArgumentException("Image object cannot be null");
    }

    IImage result = new ImageImpl(image.getWidth(), image.getHeight(), image.getMaxValue());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int[] pixel = image.getPixel(x, y);
        int grayValue = getValueToUse(pixel[0], pixel[1], pixel[2]);
        result.setPixel(x, y, grayValue, grayValue, grayValue);
      }
    }

    return result;
  }

  /**
   * Abstract method that gets the value needed to create a GreyScale IImage object.
   *
   * @param r the red component of the pixel (0 to maxValue).
   * @param g the green component of the pixel (0 to maxValue).
   * @param b the blue component of the pixel (0 to maxValue).
   * @return value of type integer to be used to create the greyscale image.
   */
  protected abstract int getValueToUse(int r, int g, int b);
}
