package operations;

/**
 * Class extends from the AbstractGrayScaleOperation.
 * It returns the intensity value and creates a greyscale IImage object based on the value.
 * Intensity value is calculated by the average of the three components for each pixel.
 */
public class IntensityGreyScaleOperation extends AbstractGrayScaleOperation {
  /**
   * Gets the value needed to create a GreyScale IImage object.
   *
   * @param r the red component of the pixel (0 to maxValue).
   * @param g the green component of the pixel (0 to maxValue).
   * @param b the blue component of the pixel (0 to maxValue).
   * @return value of type integer to be used to create the greyscale image.
   */
  @Override
  protected int getValueToUse(int r, int g, int b) {
    return (r + g + b) / 3;
  }
}
