package model;

/**
 * Interface representing an image histogram that tracks the frequency distribution
 * of pixel values for red, green, blue, and intensity components of an image.
 * Each histogram contains 256 entries (for values 0-255).
 */
public interface IImageHistogram {
  /**
   * Get the histogram array for the red component.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with red value i.
   *
   * @return a copy of the red histogram array.
   */
  int[] getRedHistogram();

  /**
   * Get the histogram array for the green component.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with green value i.
   *
   * @return a copy of the green histogram array.
   */
  int[] getGreenHistogram();

  /**
   * Get the histogram array for the blue component.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with blue value i.
   *
   * @return a copy of the blue histogram array.
   */
  int[] getBlueHistogram();

  /**
   * Get the histogram array for the intensity component.
   * The intensity is calculated as the average of the RGB components.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with intensity value i.
   *
   * @return a copy of the intensity histogram array.
   */
  int[] getIntensityHistogram();

  /**
   * Get the overall maximum count across all histograms.
   *
   * @return the maximum count value across all histograms.
   */
  int getOverallMaxCount();
}
