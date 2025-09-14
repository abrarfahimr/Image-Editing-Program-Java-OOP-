package model;

/**
 * Implementation of the ImageHistogram interface.
 * Computes and stores histogram data for red, green, blue, and intensity components
 * of an image. Each histogram contains 256 entries representing the frequency
 * of each possible value (0-255) in the image.
 */
public class ImageHistogramImpl implements IImageHistogram {
  private final int[] redHistogram;
  private final int[] greenHistogram;
  private final int[] blueHistogram;
  private final int[] intensityHistogram;

  /**
   * Constructor that computes histograms for the given image.
   *
   * @param image the image to compute histogram.
   * @throws IllegalArgumentException if image is null.
   */
  public ImageHistogramImpl(IImage image) {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }

    // Initialize histogram arrays
    this.redHistogram = new int[256];
    this.greenHistogram = new int[256];
    this.blueHistogram = new int[256];
    this.intensityHistogram = new int[256];

    // Compute histograms
    computeHistograms(image);
  }

  /**
   * Compute histograms for all components of the image.
   *
   * @param image the image to analyze.
   */
  private void computeHistograms(IImage image) {
    int width = image.getWidth();
    int height = image.getHeight();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int[] rgb = image.getPixel(x, y);

        // Extract individual components and clamp to 0-255 range
        int red = clampValue(rgb[0]);
        int green = clampValue(rgb[1]);
        int blue = clampValue(rgb[2]);

        // Calculate intensity as average of RGB components
        int intensity = clampValue((red + green + blue) / 3);

        // Increment histogram counters
        redHistogram[red]++;
        greenHistogram[green]++;
        blueHistogram[blue]++;
        intensityHistogram[intensity]++;
      }
    }
  }

  /**
   * Clamp a value to the range 0-255.
   *
   * @param value the value to clamp.
   * @return the clamped value between 0 and 255.
   */
  private int clampValue(int value) {
    if (value < 0) {
      return 0;
    }
    return Math.min(value, 255);
  }

  /**
   * Get the histogram array for the red component.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with red value i.
   *
   * @return a copy of the red histogram array.
   */
  @Override
  public int[] getRedHistogram() {
    // Return a copy to prevent external modification
    return redHistogram.clone();
  }

  /**
   * Get the histogram array for the green component.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with green value i.
   *
   * @return a copy of the green histogram array.
   */
  @Override
  public int[] getGreenHistogram() {
    // Return a copy to prevent external modification
    return greenHistogram.clone();
  }

  /**
   * Get the histogram array for the blue component.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with blue value i.
   *
   * @return a copy of the blue histogram array.
   */
  @Override
  public int[] getBlueHistogram() {
    // Return a copy to prevent external modification
    return blueHistogram.clone();
  }

  /**
   * Get the histogram array for the intensity component.
   * The intensity is calculated as the average of the RGB components.
   * The array contains 256 elements where index 'i' represents the count
   * of pixels with intensity value i.
   *
   * @return a copy of the intensity histogram array.
   */
  @Override
  public int[] getIntensityHistogram() {
    // Return a copy to prevent external modification
    return intensityHistogram.clone();
  }

  /**
   * Get the overall maximum count across all histograms.
   *
   * @return the maximum count value across all histograms.
   */
  @Override
  public int getOverallMaxCount() {
    return Math.max(Math.max(findMaxCount(redHistogram), findMaxCount(greenHistogram)),
            Math.max(findMaxCount(blueHistogram), findMaxCount(intensityHistogram)));
  }

  /**
   * Find the maximum count in a histogram array.
   *
   * @param histogram the histogram array to search
   * @return the maximum count value
   */
  private int findMaxCount(int[] histogram) {
    int max = 0;
    for (int count : histogram) {
      if (count > max) {
        max = count;
      }
    }
    return max;
  }

  /**
   * Get a string representation of the histogram statistics.
   * Useful for debugging and logging purposes.
   *
   * @return a string containing basic histogram information
   */
  @Override
  public String toString() {
    return String.format(
            "ImageHistogram[maxCount=%d, totalArrays=4]",
            getOverallMaxCount()
    );
  }
}
