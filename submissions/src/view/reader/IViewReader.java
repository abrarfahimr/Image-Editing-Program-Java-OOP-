package view.reader;

import java.io.IOException;

import model.IImage;

/**
 * Interface takes in a file and returns an object class IImage.
 * It has only 1 method read() that takes in a Readable object and returns an image of type IImage.
 * Throws and IOException if fail to read the file.
 */
public interface IViewReader {
  /**
   * Read a file and return an object of type IImage.
   *
   * @return a object of type IImage.
   * @throws IOException if fail to read the file.
   */
  IImage read() throws IOException;
}
