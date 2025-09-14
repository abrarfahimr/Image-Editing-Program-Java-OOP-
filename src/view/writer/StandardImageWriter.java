package view.writer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import model.IImage;

/**
 * Writes to new file for standard image formats (JPEG, PNG, BMP) using ImageIO.
 */
public class StandardImageWriter implements IWriter {
  private final OutputStream outputStream;
  private final String format;

  /**
   * Constructor initializes the output stream and file format to write the image.
   *
   * @param outputStream the output stream to write to.
   * @param format       the format (jpg, png, bmp).
   */
  public StandardImageWriter(OutputStream outputStream, String format) {
    this.outputStream = outputStream;
    this.format = format;
  }

  /**
   * Takes in an IImage object and write to a file.
   *
   * @param image object of type IImage.
   * @throws IllegalArgumentException if image is null.
   * @throws IOException              if fail to write to file.
   */
  @Override
  public void write(IImage image) throws IOException {
    if (image == null) {
      throw new IllegalArgumentException("Image object cannot be null");
    }
    try {
      BufferedImage bufferedImage = convertToBufferedImage(image);

      boolean writeToFile = ImageIO.write(bufferedImage, format, outputStream);
      if (!writeToFile) {
        throw new IOException("Could not write image in format: " + format);
      }
    } finally {
      outputStream.close();
    }
  }


  /**
   * Convert image of type IImage to buffered image.
   *
   * @param image of type IImage to be converted.
   * @return a buffered image.
   */
  public static BufferedImage convertToBufferedImage(IImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int[] pixel = image.getPixel(x, y);
        // pack red, green, and blue components into a single 24-bit integer
        // E.g. 0x00RRGGBB for BufferImage class to use
        int rgb = (pixel[0] << 16) | (pixel[1] << 8) | pixel[2];
        bufferedImage.setRGB(x, y, rgb);
      }
    }
    return bufferedImage;
  }
}
