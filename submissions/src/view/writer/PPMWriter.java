package view.writer;

import java.io.IOException;

import model.IImage;

/**
 * Class takes in an object of type IImage and writes to an appendable.
 * Throws an IOException if fail to write to the appendable.
 */
public class PPMWriter implements IWriter {
  private final Appendable appendable;

  /**
   * Constructor takes in an appendable of type Appendable.
   *
   * @param appendable of type Appendable to append the IImage object to.
   * @throws IllegalArgumentException if Appendable object is null.
   */
  public PPMWriter(Appendable appendable) {
    if (appendable == null) {
      throw new IllegalArgumentException("Appendable object cannot be null.");
    }
    this.appendable = appendable;
  }

  /**
   * Takes in an IImage object and write to an appendable.
   *
   * @param image object of type IImage.
   * @throws IOException if fail to write to an appendable.
   */
  @Override
  public void write(IImage image) throws IOException {
    try {
      appendable.append("P3\n");
      appendable.append(
                      String.valueOf(image.getWidth()))
              .append(" ")
              .append(String.valueOf(image.getHeight()))
              .append("\n");
      appendable.append(String.valueOf(image.getMaxValue()))
              .append("\n");

      for (int y = 0; y < image.getHeight(); y++) {
        appendable.append(" "); // leading space for rgb values in each column

        for (int x = 0; x < image.getWidth(); x++) {
          int[] pixel = image.getPixel(x, y);
          appendable.append(String.valueOf(pixel[0]))
                  .append(" ")
                  .append(String.valueOf(pixel[1]))
                  .append(" ")
                  .append(String.valueOf(pixel[2]));

          // if x does not reach the width, add a space in the middle.
          if (x < image.getWidth() - 1) {
            appendable.append(" ");
          }
        }
        appendable.append("\n"); // new line after each row
      }
    } catch (IllegalStateException e) {
      throw new IllegalStateException("write operation failed.");
    }
  }
}
