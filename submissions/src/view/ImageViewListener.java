package view;

/**
 * Interface for listening to high-level view events.
 * Controllers should implement this interface to receive notifications
 * about user actions in the view.
 */
public interface ImageViewListener {
  /**
   * Called when the user requests to load an image.
   *
   * @param filePath  the path to the image file.
   * @param imageName the name to assign to the loaded image.
   */
  void onLoadImage(String filePath, String imageName);

  /**
   * Called when the user requests to save an image.
   *
   * @param filePath  the path where the image should be saved.
   * @param imageName the name to assign to the saved image.
   */
  void onSaveImage(String filePath, String imageName);

  /**
   * Called when the user requests to delete an image.
   *
   * @param imageId   the ID of the image to delete.
   * @param imageName the name to assign to the deleted image.
   */
  void onDeleteImage(String imageId, String imageName);

  /**
   * Called when the user requests to brighten an image.
   *
   * @param imageName the name to assign to the image.
   * @param amount    the amount to brighten (can be negative to darken).
   */
  void onBrightnessOperation(String imageName, int amount);

  /**
   * Called when the user requests a grayscale operation.
   *
   * @param imageName the name to assign to the greyscale image.
   * @param operation the type of grayscale operation
   *                  (e.g., "red-component", "intensity-component").
   */
  void onGrayscaleOperation(String imageName, String operation);

  /**
   * Called when the user selects an image from already loaded images.
   *
   * @param imageName the name to assign to the displayed image.
   */
  void displaySelectedImage(String imageName);
}
