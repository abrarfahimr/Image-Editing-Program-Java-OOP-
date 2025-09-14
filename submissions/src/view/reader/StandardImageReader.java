package view.reader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import model.IImage;
import model.ImageImpl;

/**
 * Reader for standard image formats (JPEG, PNG, BMP) using ImageIO.
 */
public class StandardImageReader implements IViewReader {
  private final InputStream inputStream;
  private final String format;

  /**
   * Constructor initializes the input stream and file format to read the image.
   *
   * @param inputStream the input stream to read from.
   * @param format      the format (jpg, png, bmp).
   */
  public StandardImageReader(InputStream inputStream, String format) {
    this.inputStream = inputStream;
    this.format = format;
  }

  /**
   * Read a file and return an object of type IImage.
   *
   * @return a object of type IImage.
   * @throws IOException if fail to read the file.
   */
  @Override
  public IImage read() throws IOException {
    try {
      BufferedImage bufferedImage = ImageIO.read(inputStream);
      if (bufferedImage == null) {
        throw new IOException("Could not read image in format: " + format);
      }
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
      IImage image = new ImageImpl(width, height, 255);

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int rgb = bufferedImage.getRGB(x, y);
          // shift the rgb value to get the component (red or green or blue) individually
          // and mask it so only the lowest 8 bit remains (to get the component's integer value)
          int r = (rgb >> 16) & 0xFF;
          int g = (rgb >> 8) & 0xFF;
          int b = rgb & 0xFF;
          image.setPixel(x, y, r, g, b);
        }
      }

      return image;

    } catch (IOException e) {
      throw new IOException("Failed to read file: " + e.getMessage());
    } finally {
      inputStream.close();
    }
  }
}
