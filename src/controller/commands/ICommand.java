package controller.commands;

/**
 * Interface for the commands used for image manipulation.
 * It has only one method run that executes the image manipulation
 * using the operation classes, Brighten, darken, 6 forms of greyscale.
 */
public interface ICommand {
  /**
   * Executes the image manipulation operation.
   *
   * @throws IllegalArgumentException if command is null or not found.
   */
  void run() throws IllegalArgumentException;
}
