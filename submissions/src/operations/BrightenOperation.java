package operations;

import model.IImage;
import model.ImageImpl;

/**
 * Class implements IOperation interface.
 * It adds to the value of rgb based on the bright value.
 * If the value is greater than 0 it brightens the image,
 * if it is less than 0 it darkens and if it is 0 it does not change.
 */
public class BrightenOperation implements IOperation {
  private final int value;

  /**
   * Constructor takes in an increment of type integer.
   * It increases the rgb values of each pixel based on the increment value.
   *
   * @param value of type int used to increase/decrease the rgb value of each pixel.
   * @throws IllegalArgumentException if increment value is negative or zero.
   */
  public BrightenOperation(int value) {
    this.value = value;
  }

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
        int r;
        int g;
        int b;

        if (value > 0) {
          // set to min of maxValue or value
          r = Math.min(pixel[0] + value, image.getMaxValue());
          g = Math.min(pixel[1] + value, image.getMaxValue());
          b = Math.min(pixel[2] + value, image.getMaxValue());
        } else if (value < 0) {
          // set to max of 0 or value
          r = Math.max(pixel[0] + value, 0);
          g = Math.max(pixel[1] + value, 0);
          b = Math.max(pixel[2] + value, 0);
        } else {
          // keep the same r,g,b value
          r = pixel[0];
          g = pixel[1];
          b = pixel[2];
        }

        result.setPixel(x, y, r, g, b);
      }
    }

    return result;
  }
}
