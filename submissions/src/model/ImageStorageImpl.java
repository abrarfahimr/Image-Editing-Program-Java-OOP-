package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class implements IImageStorage interface.
 * It creates and hashmap to store images, and it's IImage object.
 */
public class ImageStorageImpl implements IImageStorage {
  private final Map<String, IImage> images;

  /**
   * Constructor initializes a new hashmap to store images, and it's IImage object.
   */
  public ImageStorageImpl() {
    this.images = new HashMap<>();
  }

  /**
   * Adds an image to the database with the given name.
   *
   * @param name  the name of the image.
   * @param image of type IImage to store.
   * @throws IllegalArgumentException if name is null/empty or image is null.
   * @throws IllegalStateException    if an image with this name already exists.
   */
  @Override
  public void addImage(String name, IImage image)
          throws IllegalArgumentException, IllegalStateException {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    if (images.containsKey(name)) {
      throw new IllegalStateException("Image with the same name already exists: " + name);
    }

    images.put(name, image);

  }

  /**
   * Retrieves an image by name.
   *
   * @param name the name of the image.
   * @return of type IImage to associated with the name.
   * @throws IllegalArgumentException if name is null/empty or no image exists with this name.
   */
  @Override
  public IImage getImage(String name) throws IllegalArgumentException {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    IImage image = images.get(name);
    if (image == null) {
      throw new IllegalArgumentException("Image object does not exist for the name: " + name);
    }

    return image;
  }

  /**
   * Checks if an image exists with the given name.
   *
   * @param name the name of the image to check.
   * @return true if an image exists with this name, false otherwise.
   */
  @Override
  public boolean hasImage(String name) {
    IImage image = images.get(name);

    return image != null;
  }

  /**
   * Overwrite an existing image or add a new one.
   *
   * @param name  the name of the image.
   * @param image of type IImage to store.
   * @throws IllegalArgumentException if name is null/empty or image is null.
   */
  @Override
  public void putImage(String name, IImage image) throws IllegalArgumentException {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    images.put(name, image);
  }

  /**
   * Gets all image names currently stored.
   *
   * @return the hashmap of all image names, and it's IImage object.
   */
  @Override
  public Map<String, IImage> getAllImages() {
    return this.images;
  }

  /**
   * Remove an image from database.
   *
   * @param name name of the image to delete.
   */
  @Override
  public void removeImage(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    if (!images.containsKey(name)) {
      throw new IllegalArgumentException("Image '" + name + "' does not exist");
    }

    images.remove(name);
  }
}
