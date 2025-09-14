package operations;

/**
 * Class extends from the AbstractGrayScaleOperation.
 * It returns the red value and creates a greyscale IImage object based on the value.
 */
public class RedGreyScaleOperation extends AbstractGrayScaleOperation {
  /**
   * Gets the value needed to create a GreyScale IImage object.
   *
   * @param r the red component of the pixel (0 to maxValue).
   * @param g the green component of the pixel (0 to maxValue).
   * @param b the blue component of the pixel (0 to maxValue).
   * @return red value of type integer.
   */
  @Override
  protected int getValueToUse(int r, int g, int b) {
    return r;
  }
}
