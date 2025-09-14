package view.reader;

import java.io.IOException;
import java.util.Scanner;

import model.IImage;
import model.ImageImpl;

/**
 * Class takes in a PPM file and returns an object of type IImage.
 * Throws and IOException if fail to read the PPM file.
 */
public class PPMReader implements IViewReader {
  private final Readable readable;

  /**
   * Constructor takes in a readable object of type Readable.
   * It reads the readable objects and creates an IImage object.
   *
   * @param readable object of type Readable.
   * @throws IllegalArgumentException if readable object is null.
   */
  public PPMReader(Readable readable) {
    if (readable == null) {
      throw new IllegalArgumentException("Readable object cannot be null.");
    }
    this.readable = readable;
  }

  /**
   * Helper method that takes in the scanner and checks if the next input is valid or not.
   * If the next input has an "#", then it is a comment and it ignored.
   *
   * @param scanner of type Scanner that scans for the input.
   * @return a String of the input.
   * @throws IOException if fail to read the input.
   */
  private String readNextToken(Scanner scanner) throws IOException {
    while (scanner.hasNext()) {
      if (scanner.hasNext("#.*")) {
        scanner.nextLine(); // skip the line if it's a comment
      } else {
        return scanner.next();
      }
    }
    throw new IOException("Unexpected end of file");
  }

  /**
   * Read a file and return an object of type IImage.
   *
   * @return a object of type IImage.
   * @throws IOException if fail to read the file.
   */
  @Override
  public IImage read() throws IOException {
    Scanner scanner = new Scanner(readable);

    String token = readNextToken(scanner);
    if (!token.equals("P3")) {
      throw new IOException("Invalid PPM file");
    }

    int width = Integer.parseInt(readNextToken(scanner));
    int height = Integer.parseInt(readNextToken(scanner));
    int maxValue = Integer.parseInt(readNextToken(scanner));

    IImage image = new ImageImpl(width, height, maxValue);

    // Set Pixel
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = Integer.parseInt(readNextToken(scanner));
        int g = Integer.parseInt(readNextToken(scanner));
        int b = Integer.parseInt(readNextToken(scanner));

        image.setPixel(x, y, r, g, b);
      }
    }

    return image;
  }
}
