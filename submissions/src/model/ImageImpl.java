package model;

/**
 * Class implements IImage interface. It creates a class representing an image.
 * Each pixel contains the x and y coordinates and the rbg values of that pixel.
 * Other methods include getters for getting the x,y coordinate, height, width, and max color value.
 */
public class ImageImpl implements IImage {
  private final int width;
  private final int height;
  private final int maxValue;
  private final int[] pixels; // pixel contain the rbg values [r0,g0,b0,r1,g1,b1]

  /**
   * Constructor takes in a width, height,
   * and maxValue of the image to create the image of type IImage.
   *
   * @param width    of the image of type integer.
   * @param height   of the image of type integer.
   * @param maxValue of the image components of type integer.
   * @throws IllegalArgumentException if dimensions are 0 or negative.
   */
  public ImageImpl(int width, int height, int maxValue) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Dimensions must be non-zero and positive");
    }
    if (maxValue <= 0) {
      throw new IllegalArgumentException("Max color value must be non-zero and positive");
    }

    this.width = width;
    this.height = height;
    this.maxValue = maxValue;
    pixels = new int[width * height * 3]; // 3 for the rgb values
  }

  /**
   * Helper function get the starting index of rgb value of a given coordinate.
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the first index of rbg value of a given coordinate.
   */
  private int getPixelIndex(int x, int y) {
    return (y * width + x) * 3;
  }

  /**
   * Set the rgb values of a pixel on a certain x and y coordinate.
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @param r the red component of the pixel (0 to maxValue).
   * @param g the green component of the pixel (0 to maxValue).
   * @param b the blue component of the pixel (0 to maxValue).
   * @throws IllegalArgumentException if x,y,r,g,b are out of range.
   */
  @Override
  public void setPixel(int x, int y, int r, int g, int b) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      throw new IllegalArgumentException("Coordinates out of range");
    }

    if (r < 0 || r > maxValue || g < 0 || g > maxValue || b < 0 || b > maxValue) {
      throw new IllegalArgumentException("Color values out of range");
    }

    int startingIndex = getPixelIndex(x, y);
    pixels[startingIndex] = r;
    pixels[startingIndex + 1] = g;
    pixels[startingIndex + 2] = b;
  }

  /**
   * Get the r,g,b values of a pixel as an array[] of integers.
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the r,g,b values of a pixel as an array[] of integers.
   * @throws IllegalArgumentException if x,y are out of range.
   */
  @Override
  public int[] getPixel(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      throw new IllegalArgumentException("Coordinates out of range");
    }

    int startingIndex = getPixelIndex(x, y);
    return new int[]{pixels[startingIndex], pixels[startingIndex + 1], pixels[startingIndex + 2]};
  }

  /**
   * Get the width of the image.
   *
   * @return the width of the image.
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * Get the height of the image.
   *
   * @return the height of the image.
   */
  @Override
  public int getHeight() {
    return this.height;
  }

  /**
   * get the max color value of the image.
   * E.g. For 8 bit image it's 255.
   *
   * @return max color value of the image.
   */
  @Override
  public int getMaxValue() {
    return this.maxValue;
  }
}
