package operations;

/**
 * Class extends from the AbstractGrayScaleOperation.
 * It returns the intensity value and creates a greyscale IImage object based on the value.
 * Luma is calculated by the weighted sum of 0.2126 * r + 0.7152 * g + 0.0722 * b.
 */
public class LumaGreyScaleOperation extends AbstractGrayScaleOperation {
  /**
   * Abstract method that gets the value needed to create a GreyScale IImage object.
   *
   * @param r the red component of the pixel (0 to maxValue).
   * @param g the green component of the pixel (0 to maxValue).
   * @param b the blue component of the pixel (0 to maxValue).
   * @return value of type integer to be used to create the greyscale image.
   */
  @Override
  protected int getValueToUse(int r, int g, int b) {
    return (int) (0.2126 * r + 0.7152 * g + 0.0722 * b); // cast double type to int
  }
}
