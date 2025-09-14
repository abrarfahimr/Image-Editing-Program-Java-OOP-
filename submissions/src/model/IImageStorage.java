package model;

import java.util.Map;

/**
 * Interface for managing multiple images in the application.
 * Provides storage and retrieval of images and their IImage objects.
 */
public interface IImageStorage {
  /**
   * Adds an image to the database with the given name.
   *
   * @param name  the name of the image.
   * @param image of type IImage to store.
   * @throws IllegalArgumentException if name is null/empty or image is null.
   * @throws IllegalStateException    if an image with this name already exists.
   */
  void addImage(String name, IImage image) throws IllegalArgumentException, IllegalStateException;

  /**
   * Retrieves an image by name.
   *
   * @param name the name of the image.
   * @return of type IImage to associated with the name.
   * @throws IllegalArgumentException if name is null/empty or no image exists with this name.
   */
  IImage getImage(String name) throws IllegalArgumentException;

  /**
   * Checks if an image exists with the given name.
   *
   * @param name the name of the image to check.
   * @return true if an image exists with this name, false otherwise.
   */
  boolean hasImage(String name);

  /**
   * Overwrite an existing image or add a new one.
   *
   * @param name  the name of the image.
   * @param image of type IImage to store.
   * @throws IllegalArgumentException if name is null/empty or image is null.
   */
  void putImage(String name, IImage image) throws IllegalArgumentException;

  /**
   * Gets all image names currently stored.
   *
   * @return the hashmap of all image names and it's IImage object.
   */
  Map<String, IImage> getAllImages();

  /**
   * Remove an image from database.
   *
   * @param name name of the image to delete.
   */
  void removeImage(String name);
}
