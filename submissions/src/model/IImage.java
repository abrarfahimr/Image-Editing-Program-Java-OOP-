package model;

/**
 * Abrar Aurnab, 002039241.
 * CS5004, Summer 2025.
 * ----------------------------------.
 * Interface for the Image class.
 * It contains the x and y coordinate of the pixel and the values of that pixels rgb values.
 */
public interface IImage {
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
  void setPixel(int x, int y, int r, int g, int b) throws IllegalArgumentException;

  /**
   * Get the r,g,b values of a pixel as an array[] of integers.
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the r,g,b values of a pixel as an array[] of integers.
   * @throws IllegalArgumentException if x,y are out of range.
   */
  int[] getPixel(int x, int y) throws IllegalArgumentException;

  /**
   * Get the width of the image.
   *
   * @return the width of the image.
   */
  int getWidth();

  /**
   * Get the height of the image.
   *
   * @return the height of the image.
   */
  int getHeight();

  /**
   * get the max color value of the image.
   * E.g. For 8 bit image it's 255.
   *
   * @return max color value of the image.
   */
  int getMaxValue();
}
