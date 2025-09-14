package view.writer;

import java.io.IOException;

import model.IImage;

/**
 * Interface takes in an object of type IImage and writes to a file.
 * It has only 1 method write() that takes in an IImage object and writes to a file.
 * Throws an IOException if fail to write to file.
 */
public interface IWriter {
  /**
   * Takes in an IImage object and write to a file.
   *
   * @param image object of type IImage.
   * @throws IOException if fail to write to file.
   */
  void write(IImage image) throws IOException;
}
