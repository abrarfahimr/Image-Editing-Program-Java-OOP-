package controller;

/**
 * Command Interface to execute the image manipulation program.
 * Has only one operation "run" which run the program taking in a Readable and Appendable object.
 */
public interface IImageController {
  /**
   * Method executes the image manipulate program.
   *
   * @throws IllegalStateException if fail to read or write from or to inputs.
   */
  void run() throws IllegalStateException;
}
