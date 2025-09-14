package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import model.IImage;
import model.IImageStorage;
import model.ImageStorageImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests controller, checking if the images are generated properly in the res folder.
 */
public class ImageGenerationTest {
  private IImageStorage imageDatabase;
  private StringBuilder output;
  private IImageController controller;

  @Before
  public void setUp() {
    imageDatabase = new ImageStorageImpl();
    output = new StringBuilder();
  }

  /**
   * Test on JPEG Image.
   */
  @Test
  public void testCommandOnJPG() {
    String command = "load res/my_cat.jpeg my-cat\n"
            + "brighten 100 my-cat my_cat-bright100\n"
            + "brighten -100 my-cat my_cat-darken100\n"
            + "red-component my-cat my-cat-redGreyScale\n"
            + "green-component my-cat my-cat-greenGreyScale\n"
            + "blue-component my-cat my-cat-blueGreyScale\n"
            + "value-component my-cat my-cat-valueGreyScale\n"
            + "intensity-component my-cat my-cat-intensityGreyScale\n"
            + "luma-component my-cat my-cat-lumaGreyScale\n"
            + "save res/my_cat-bright100.jpeg my_cat-bright100\n"
            + "save res/my_cat-darken100.jpeg my_cat-darken100\n"
            + "save res/my-cat-redGreyScale.jpeg my-cat-redGreyScale\n"
            + "save res/my-cat-greenGreyScale.jpeg my-cat-greenGreyScale\n"
            + "save res/my-cat-blueGreyScale.jpeg my-cat-blueGreyScale\n"
            + "save res/my-cat-valueGreyScale.jpeg my-cat-valueGreyScale\n"
            + "save res/my-cat-intensityGreyScale.jpeg my-cat-intensityGreyScale\n"
            + "save res/my-cat-lumaGreyScale.jpeg my-cat-lumaGreyScale\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    IImage image = imageDatabase.getImage("my-cat");
    IImage brightenImage = imageDatabase.getImage("my_cat-bright100");
    assertEquals(image.getWidth(), brightenImage.getWidth());
    assertEquals(image.getHeight(), brightenImage.getHeight());
    assertEquals(image.getMaxValue(), brightenImage.getMaxValue());

  }

  /**
   * Test on PNG Image.
   */
  @Test
  public void testCommandOnPNG() {
    String command = "load res/my_cat.png my-cat\n"
            + "brighten 100 my-cat my_cat-bright100\n"
            + "brighten -100 my-cat my_cat-darken100\n"
            + "red-component my-cat my-cat-redGreyScale\n"
            + "green-component my-cat my-cat-greenGreyScale\n"
            + "blue-component my-cat my-cat-blueGreyScale\n"
            + "value-component my-cat my-cat-valueGreyScale\n"
            + "intensity-component my-cat my-cat-intensityGreyScale\n"
            + "luma-component my-cat my-cat-lumaGreyScale\n"
            + "save res/my_cat-bright100.png my_cat-bright100\n"
            + "save res/my_cat-darken100.png my_cat-darken100\n"
            + "save res/my-cat-redGreyScale.png my-cat-redGreyScale\n"
            + "save res/my-cat-greenGreyScale.png my-cat-greenGreyScale\n"
            + "save res/my-cat-blueGreyScale.png my-cat-blueGreyScale\n"
            + "save res/my-cat-valueGreyScale.png my-cat-valueGreyScale\n"
            + "save res/my-cat-intensityGreyScale.png my-cat-intensityGreyScale\n"
            + "save res/my-cat-lumaGreyScale.png my-cat-lumaGreyScale\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    IImage image = imageDatabase.getImage("my-cat");
    IImage brightenImage = imageDatabase.getImage("my_cat-bright100");
    assertEquals(image.getWidth(), brightenImage.getWidth());
    assertEquals(image.getHeight(), brightenImage.getHeight());
    assertEquals(image.getMaxValue(), brightenImage.getMaxValue());
  }

  /**
   * Test on BMP Image.
   */
  @Test
  public void testCommandOnBMP() {
    String command = "load res/my_cat.bmp my-cat\n"
            + "brighten 100 my-cat my_cat-bright100\n"
            + "brighten -100 my-cat my_cat-darken100\n"
            + "red-component my-cat my-cat-redGreyScale\n"
            + "green-component my-cat my-cat-greenGreyScale\n"
            + "blue-component my-cat my-cat-blueGreyScale\n"
            + "value-component my-cat my-cat-valueGreyScale\n"
            + "intensity-component my-cat my-cat-intensityGreyScale\n"
            + "luma-component my-cat my-cat-lumaGreyScale\n"
            + "save res/my_cat-bright100.bmp my_cat-bright100\n"
            + "save res/my_cat-darken100.bmp my_cat-darken100\n"
            + "save res/my-cat-redGreyScale.bmp my-cat-redGreyScale\n"
            + "save res/my-cat-greenGreyScale.bmp my-cat-greenGreyScale\n"
            + "save res/my-cat-blueGreyScale.bmp my-cat-blueGreyScale\n"
            + "save res/my-cat-valueGreyScale.bmp my-cat-valueGreyScale\n"
            + "save res/my-cat-intensityGreyScale.bmp my-cat-intensityGreyScale\n"
            + "save res/my-cat-lumaGreyScale.bmp my-cat-lumaGreyScale\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    IImage image = imageDatabase.getImage("my-cat");
    IImage brightenImage = imageDatabase.getImage("my_cat-bright100");
    assertEquals(image.getWidth(), brightenImage.getWidth());
    assertEquals(image.getHeight(), brightenImage.getHeight());
    assertEquals(image.getMaxValue(), brightenImage.getMaxValue());
  }
}
