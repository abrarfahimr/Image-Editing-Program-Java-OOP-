package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.IImage;
import model.IImageStorage;

/**
 * Mock implementation of IImageStorage for testing controller parsing.
 * Logs all method calls for verification.
 */
public class MockImageStorage implements IImageStorage {
  private final List<String> log;
  private final Map<String, IImage> images;

  /**
   * Constructor initializes the log and image storage.
   */
  public MockImageStorage() {
    this.log = new ArrayList<>();
    this.images = new HashMap<>();
  }

  /**
   * Adds an image to the database with the given name.
   *
   * @param name  the name of the image.
   * @param image of type IImage to store.
   * @throws IllegalStateException if an image with this name already exists.
   */
  @Override
  public void addImage(String name, IImage image) {
    log.add("addImage: " + name);
    if (images.containsKey(name)) {
      throw new IllegalStateException("Image already exists: " + name);
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
  public IImage getImage(String name) {
    log.add("getImage: " + name);
    IImage image = images.get(name);
    if (image == null) {
      throw new IllegalArgumentException("No image found: " + name);
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
    log.add("hasImage: " + name);
    return images.containsKey(name);
  }

  /**
   * Overwrite an existing image or add a new one.
   *
   * @param name  the name of the image.
   * @param image of type IImage to store.
   */
  @Override
  public void putImage(String name, IImage image) {
    log.add("putImage: " + name);
    images.put(name, image);
  }

  /**
   * Gets all image names currently stored.
   *
   * @return the hashmap of all image names and it's IImage object.
   */
  @Override
  public Map<String, IImage> getAllImages() {
    log.add("getAllImages");
    return new HashMap<>(images);
  }

  /**
   * Get all the logs.
   *
   * @return List of Strings of logs.
   */
  public List<String> getLog() {
    return this.log;
  }

}