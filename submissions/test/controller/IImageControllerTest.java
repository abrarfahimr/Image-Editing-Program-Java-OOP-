package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import controller.commands.BlueGreyScaleCommand;
import controller.commands.BrightenCommand;
import controller.commands.GreenGreyScaleCommand;
import controller.commands.ICommand;
import controller.commands.IntensityGreyScaleCommand;
import controller.commands.LumaGreyScaleCommand;
import controller.commands.RedGreyScaleCommand;
import controller.commands.ValueGreyScaleCommand;
import model.IImage;
import model.IImageStorage;
import model.ImageImpl;
import model.ImageStorageImpl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Extensive test cases to test the controller and Commands using scripts.
 * It contains test for all commands, edge cases such as null value,
 * check and show correct errors when the input is incorrect.
 * The test use StringReaders and StringBuilder to create test scripts
 * It also uses a mock model to check if the controller is taking the correct inputs.
 * The last few test cases contains test to generate image in the /res folder using the controller.
 */
public class IImageControllerTest {
  private IImageStorage imageDatabase;
  private IImage testImage;
  private IImage testImage2;
  private StringBuilder output;
  private IImageController controller;
  private MockImageStorage mockStorage;

  @Before
  public void setUp() {
    imageDatabase = new ImageStorageImpl();
    mockStorage = new MockImageStorage();

    testImage = new ImageImpl(2, 2, 255);
    testImage.setPixel(0, 0, 100, 150, 200);
    testImage.setPixel(1, 0, 50, 100, 150);
    testImage.setPixel(0, 1, 200, 100, 50);
    testImage.setPixel(1, 1, 0, 0, 0);

    testImage2 = new ImageImpl(2, 2, 255);
    testImage2.setPixel(0, 0, 50, 80, 180);
    testImage2.setPixel(1, 0, 200, 40, 80);
    testImage2.setPixel(0, 1, 75, 100, 200);
    testImage2.setPixel(1, 1, 5, 5, 5);

    output = new StringBuilder();
  }

  /**
   * Command Tests.
   */
  // Null values and edge cases
  @Test(expected = NullPointerException.class)
  public void testCommandOnNullDatabase() {
    ICommand brighten = new BrightenCommand(
            50, "test", "test-bright", null);
    brighten.run();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCommandOnNullImage() {
    imageDatabase.putImage("test", null);

    ICommand brighten = new BrightenCommand(50, "test", "test-bright", imageDatabase);
    brighten.run();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceNameNull() {
    imageDatabase.putImage(null, testImage);

    ICommand brighten = new BrightenCommand(50, "test", "test-bright", imageDatabase);
    brighten.run();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceNameEmpty() {
    imageDatabase.putImage(null, testImage);

    ICommand brighten = new BrightenCommand(
            50, "", "test-bright", imageDatabase);
    brighten.run();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationNameEmpty() {
    imageDatabase.putImage(null, testImage);

    ICommand brighten = new BrightenCommand(50, "test", "", imageDatabase);
    brighten.run();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDestinationNull() {
    imageDatabase.putImage(null, testImage);

    ICommand brighten = new BrightenCommand(50, "test", null, imageDatabase);
    brighten.run();
  }

  // Brighten Command

  @Test(expected = IllegalArgumentException.class)
  public void testBrightenFileDoesNotExist() {
    IImage result = imageDatabase.getImage("test-brighten-clamp");
  }

  @Test
  public void testBrightenCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand brighten = new BrightenCommand(50, "test", "test-bright", imageDatabase);
    brighten.run();

    assertTrue(imageDatabase.hasImage("test-bright"));
  }

  @Test
  public void testBrightenCommandClamping() {
    imageDatabase.putImage("test", testImage);

    ICommand brighten = new BrightenCommand(
            100, "test", "test-bright-clamp", imageDatabase);
    brighten.run();

    IImage result = imageDatabase.getImage("test-bright-clamp");

    assertArrayEquals(new int[]{200, 250, 255}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{150, 200, 250}, result.getPixel(1, 0));
  }

  @Test
  public void testBrightenCommandLargeValue() {
    imageDatabase.putImage("test", testImage);

    ICommand brighten = new BrightenCommand(
            1000, "test", "test-bright-clamp", imageDatabase);
    brighten.run();

    IImage result = imageDatabase.getImage("test-bright-clamp");

    assertArrayEquals(new int[]{255, 255, 255}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{255, 255, 255}, result.getPixel(1, 0));
  }

  // Darken Command
  @Test
  public void testDarkenCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand brighten = new BrightenCommand(
            -50, "test", "test-darken", imageDatabase);
    brighten.run();

    IImage result = imageDatabase.getImage("test-darken");

    assertArrayEquals(new int[]{50, 100, 150}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{0, 50, 100}, result.getPixel(1, 0));
  }

  @Test
  public void testDarkenCommandClamping() {
    imageDatabase.putImage("test", testImage);

    ICommand brighten = new BrightenCommand(
            -100, "test", "test-darken-clamp", imageDatabase);
    brighten.run();

    IImage result = imageDatabase.getImage("test-darken-clamp");

    assertArrayEquals(new int[]{0, 50, 100}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{0, 0, 50}, result.getPixel(1, 0));
  }

  @Test
  public void testDarkenCommandLargeValue() {
    imageDatabase.putImage("test", testImage);

    ICommand brighten = new BrightenCommand(
            -1000, "test", "test-darken-clamp", imageDatabase);
    brighten.run();

    IImage result = imageDatabase.getImage("test-darken-clamp");

    assertArrayEquals(new int[]{0, 0, 0}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{0, 0, 0}, result.getPixel(1, 0));
  }

  // Red Component Command
  @Test
  public void testRedGrayscaleCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand greyScale = new RedGreyScaleCommand(
            "test", "test-red-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-red-component");

    assertArrayEquals(new int[]{100, 100, 100}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{50, 50, 50}, result.getPixel(1, 0));
  }

  // Green Component Command
  @Test
  public void testGreenGrayscaleCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand greyScale = new GreenGreyScaleCommand(
            "test", "test-green-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-green-component");

    assertArrayEquals(new int[]{150, 150, 150}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 100, 100}, result.getPixel(1, 0));
  }

  // Blue Component Command
  @Test
  public void testBlueGrayscaleCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand greyScale = new BlueGreyScaleCommand(
            "test", "test-blue-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-blue-component");

    assertArrayEquals(new int[]{200, 200, 200}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{150, 150, 150}, result.getPixel(1, 0));
  }

  // Value Component Command
  @Test
  public void testValueGrayscaleCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand greyScale = new ValueGreyScaleCommand(
            "test", "test-value-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-value-component");

    assertArrayEquals(new int[]{200, 200, 200}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{150, 150, 150}, result.getPixel(1, 0));
  }

  // Intensity Component Command
  @Test
  public void testIntensityGrayscaleCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand greyScale = new IntensityGreyScaleCommand(
            "test", "test-intensity-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-intensity-component");

    assertArrayEquals(new int[]{150, 150, 150}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 100, 100}, result.getPixel(1, 0));
  }

  // Luma Component Command
  @Test
  public void testLumaGrayscaleCommand() {
    imageDatabase.putImage("test", testImage);

    ICommand greyScale = new LumaGreyScaleCommand(
            "test", "test-luma-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-luma-component");

    assertArrayEquals(new int[]{142, 142, 142}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{92, 92, 92}, result.getPixel(1, 0));
  }

  @Test
  public void testGreyScaleCommandOnBlackPixel() {
    IImage testImage1;

    testImage1 = new ImageImpl(2, 2, 255);
    testImage1.setPixel(0, 0, 255, 255, 255);
    testImage1.setPixel(1, 0, 255, 255, 255);
    testImage1.setPixel(0, 1, 255, 255, 255);
    testImage1.setPixel(1, 1, 255, 255, 255);

    imageDatabase.putImage("test", testImage1);

    ICommand greyScale = new LumaGreyScaleCommand(
            "test", "test-luma-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-luma-component");

    assertArrayEquals(new int[]{254, 254, 254}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{254, 254, 254}, result.getPixel(1, 0));
  }

  @Test
  public void testGreyScaleCommandOnWhitePixel() {
    IImage testImage1;

    testImage1 = new ImageImpl(2, 2, 255);
    testImage1.setPixel(0, 0, 0, 0, 0);
    testImage1.setPixel(1, 0, 0, 0, 0);
    testImage1.setPixel(0, 1, 0, 0, 0);
    testImage1.setPixel(1, 1, 0, 0, 0);

    imageDatabase.putImage("test", testImage1);

    ICommand greyScale = new LumaGreyScaleCommand(
            "test", "test-luma-component", imageDatabase);
    greyScale.run();

    IImage result = imageDatabase.getImage("test-luma-component");

    assertArrayEquals(new int[]{0, 0, 0}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{0, 0, 0}, result.getPixel(1, 0));
  }

  /**
   * Controller Tests.
   */
  @Test
  public void testController() {
    // Load an image for testing
    imageDatabase.putImage("test", testImage);

    String command = "brighten 50 test test-bright\n";

    StringReader reader = new StringReader(command);
    controller = new ImageControllerImpl(reader, output, imageDatabase);
    controller.run();

    assertTrue(imageDatabase.hasImage("test-bright"));

    IImage result = imageDatabase.getImage("test-bright");

    assertArrayEquals(new int[]{150, 200, 250}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 150, 200}, result.getPixel(1, 0));
  }

  @Test
  public void testControllerWithWrongInput() {
    // Load an image for testing
    imageDatabase.putImage("test", testImage);

    String command = "lighten 50 test test-bright\n";

    StringReader reader = new StringReader(command);
    controller = new ImageControllerImpl(reader, output, imageDatabase);
    controller.run();

    assertEquals("Error: Unknown command 'lighten'\n", output.toString());
  }

  @Test
  public void testBrightenMissingAllArguments() {
    String command = "brighten\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertTrue(output.toString().contains("Error"));
    assertTrue(output.toString().contains("Usage: brighten increment image-name dest-image-name"));
  }

  @Test
  public void testBrightenMissingDestination() {
    imageDatabase.putImage("test", testImage);
    String command = "brighten 50 test\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: Wrong inputs. Usage: brighten increment image-name dest-image-name\n",
            output.toString());
  }

  @Test
  public void testComponentCommandMissingDestination() {
    imageDatabase.putImage("test", testImage);
    String command = "red-component test\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: Wrong inputs. Usage: red-component image-name dest-image-name\n",
            output.toString());
  }

  @Test
  public void testBrightenWithNonIntegerIncrement() {
    imageDatabase.putImage("test", testImage);
    String command = "brighten abc test test-bright\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: increment must be an integer\n",
            output.toString());
  }

  @Test
  public void testBrightenWithZeroIncrement() {
    imageDatabase.putImage("test", testImage);
    String command = "brighten 0 test test-same\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertTrue(imageDatabase.hasImage("test-same"));
    IImage result = imageDatabase.getImage("test-same");

    assertArrayEquals(new int[]{100, 150, 200}, result.getPixel(0, 0));
  }

  @Test
  public void testBrightenWithDecimalIncrement() {
    imageDatabase.putImage("test", testImage);
    String command = "brighten 50.5 test test-bright\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: increment must be an integer\n",
            output.toString());
  }

  @Test
  public void testOperationOnNonExistentImage() {
    String command = "brighten 50 nonexistent result\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: Image object does not exist for the name: nonexistent\n",
            output.toString());
  }

  @Test
  public void testChainedOperationsWithMissingImage() {
    imageDatabase.putImage("test", testImage);
    String command = "brighten 50 test bright\n" +
            "red-component nonexistent red\n" +
            "green-component test green\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    // First and third should succeed, second should fail
    assertTrue(imageDatabase.hasImage("bright"));
    assertFalse(imageDatabase.hasImage("red"));
    assertTrue(imageDatabase.hasImage("green"));

    assertEquals(
            "Error: Image object does not exist for the name: nonexistent\n",
            output.toString());
  }

  @Test
  public void testControllerWithCommends() {
    // Load an image for testing
    imageDatabase.putImage("test", testImage);

    String command = "# This is a comment\n" +
            "brighten 50 test test-bright\n" +
            "# This is another comment\n";

    StringReader reader = new StringReader(command);
    controller = new ImageControllerImpl(reader, output, imageDatabase);
    controller.run();

    assertTrue(imageDatabase.hasImage("test-bright"));

    IImage result = imageDatabase.getImage("test-bright");

    assertArrayEquals(new int[]{150, 200, 250}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 150, 200}, result.getPixel(1, 0));
  }

  @Test
  public void testControllerWithEmptyLines() {
    // Load an image for testing
    imageDatabase.putImage("test", testImage);

    String command = "\n" +
            "brighten 50 test test-bright\n" +
            "\n";

    StringReader reader = new StringReader(command);
    controller = new ImageControllerImpl(reader, output, imageDatabase);
    controller.run();

    assertTrue(imageDatabase.hasImage("test-bright"));

    IImage result = imageDatabase.getImage("test-bright");

    assertArrayEquals(new int[]{150, 200, 250}, result.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 150, 200}, result.getPixel(1, 0));
  }

  @Test
  public void testControllerWithMultipleChanges() {
    // Load an image for testing
    imageDatabase.putImage("test", testImage);

    String command = "brighten 50 test test-bright\n" +
            "red-component test test-red-component\n";

    StringReader reader = new StringReader(command);
    controller = new ImageControllerImpl(reader, output, imageDatabase);
    controller.run();

    assertTrue(imageDatabase.hasImage("test-bright"));
    assertTrue(imageDatabase.hasImage("test-red-component"));

    IImage result1 = imageDatabase.getImage("test-bright");
    IImage result2 = imageDatabase.getImage("test-red-component");

    assertArrayEquals(new int[]{150, 200, 250}, result1.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 100, 100}, result2.getPixel(0, 0));
  }

  @Test
  public void testControllerWithTwoImages() {
    // Load an image for testing
    imageDatabase.putImage("test", testImage);
    imageDatabase.putImage("test2", testImage2);

    String command = "brighten 50 test test-bright\n" +
            "red-component test test-red-component\n" +
            "brighten 50 test2 test2-bright\n" +
            "red-component test2 test2-red-component\n";

    StringReader reader = new StringReader(command);
    controller = new ImageControllerImpl(reader, output, imageDatabase);
    controller.run();

    assertTrue(imageDatabase.hasImage("test-bright"));
    assertTrue(imageDatabase.hasImage("test-red-component"));
    assertTrue(imageDatabase.hasImage("test2-bright"));
    assertTrue(imageDatabase.hasImage("test2-red-component"));

    IImage result1 = imageDatabase.getImage("test-bright");
    IImage result2 = imageDatabase.getImage("test-red-component");
    IImage result3 = imageDatabase.getImage("test2-bright");
    IImage result4 = imageDatabase.getImage("test2-red-component");

    assertArrayEquals(new int[]{150, 200, 250}, result1.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 100, 100}, result2.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 130, 230}, result3.getPixel(0, 0));
    assertArrayEquals(new int[]{50, 50, 50}, result4.getPixel(0, 0));
  }

  @Test
  public void testOverwriteExistingImage() {
    imageDatabase.putImage("original", testImage);
    imageDatabase.putImage("original2", testImage2);

    String command = "brighten 50 original2 original\n"; // Overwrite "original" image

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    IImage result = imageDatabase.getImage("original");

    assertArrayEquals(new int[]{100, 130, 230}, result.getPixel(0, 0));
  }

  @Test
  public void testSelfModification() {
    imageDatabase.putImage("test", testImage);

    String command = "brighten 50 test test\n"; // Brighten and overwrite itself

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    IImage result = imageDatabase.getImage("test");
    assertArrayEquals(new int[]{150, 200, 250}, result.getPixel(0, 0));
  }

  @Test
  public void testAllGrayscaleOperations() {
    imageDatabase.putImage("test", testImage);

    String command = "red-component test red\n" +
            "green-component test green\n" +
            "blue-component test blue\n" +
            "value-component test value\n" +
            "intensity-component test intensity\n" +
            "luma-component test luma\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    // Check red component
    IImage red = imageDatabase.getImage("red");
    assertArrayEquals(new int[]{100, 100, 100}, red.getPixel(0, 0));
    assertArrayEquals(new int[]{200, 200, 200}, red.getPixel(0, 1));

    // Check green component
    IImage green = imageDatabase.getImage("green");
    assertArrayEquals(new int[]{150, 150, 150}, green.getPixel(0, 0));
    assertArrayEquals(new int[]{100, 100, 100}, green.getPixel(0, 1));

    // Check blue component
    IImage blue = imageDatabase.getImage("blue");
    assertArrayEquals(new int[]{200, 200, 200}, blue.getPixel(0, 0));
    assertArrayEquals(new int[]{50, 50, 50}, blue.getPixel(0, 1));

    // Check value
    IImage value = imageDatabase.getImage("value");
    assertArrayEquals(new int[]{200, 200, 200}, value.getPixel(0, 0));
    assertArrayEquals(new int[]{200, 200, 200}, value.getPixel(0, 1));

    // Check intensity
    IImage intensity = imageDatabase.getImage("intensity");
    int avg00 = (100 + 150 + 200) / 3;
    assertArrayEquals(new int[]{avg00, avg00, avg00}, intensity.getPixel(0, 0));

    // Check luma
    IImage luma = imageDatabase.getImage("luma");
    int luma00 = (int) (0.2126 * 100 + 0.7152 * 150 + 0.0722 * 200);
    assertArrayEquals(new int[]{luma00, luma00, luma00}, luma.getPixel(0, 0));
  }

  @Test
  public void testLoadCommandMissingArguments() {
    String command = "load\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: Wrong inputs. Usage: load image-path image-name\n", output.toString());
  }

  @Test
  public void testSaveCommandMissingArguments() {
    String command = "save output.png\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: Wrong inputs. Usage: save image-path image-name\n", output.toString());
  }

  @Test
  public void testSaveNonExistentImage() {
    String command = "save output.png nonexistent\n";

    controller = new ImageControllerImpl(new StringReader(command), output, imageDatabase);
    controller.run();

    assertEquals(
            "Error: Image object does not exist for the name: nonexistent\n", output.toString());
  }

  // Set Controller parsing with mock database
  @Test
  public void testControllerParsesLoadCommand() {
    mockStorage.putImage("test", new ImageImpl(1, 1, 255));
    mockStorage.getLog().clear(); // clear the logs during initializing

    String input = "brighten 50 test brightened\n";

    controller = new ImageControllerImpl(
            new StringReader(input), output, mockStorage);

    controller.run();

    List<String> logs = mockStorage.getLog();

    assertEquals(2, logs.size()); // getImage, putImage
    assertTrue(logs.contains("getImage: test"));
    assertTrue(logs.contains("putImage: brightened"));
  }

  @Test
  public void testDarkenCommandParsing() {
    mockStorage.putImage("original", testImage);
    mockStorage.getLog().clear();

    String input = "brighten -50 original darkened\n";
    controller = new ImageControllerImpl(new StringReader(input), output, mockStorage);
    controller.run();

    List<String> logs = mockStorage.getLog();

    assertEquals(2, logs.size());
    assertEquals("getImage: original", logs.get(0));
    assertEquals("putImage: darkened", logs.get(1));
  }

  @Test
  public void testRedComponentParsing() {
    mockStorage.putImage("color-img", testImage);
    mockStorage.getLog().clear();

    String input = "red-component color-img red-only\n";
    controller = new ImageControllerImpl(new StringReader(input), output, mockStorage);
    controller.run();

    List<String> logs = mockStorage.getLog();

    assertEquals(2, logs.size());
    assertEquals("getImage: color-img", logs.get(0));
    assertEquals("putImage: red-only", logs.get(1));
  }

  @Test
  public void testParsingOnGrayscaleOperations() {
    mockStorage.putImage("img", testImage);
    mockStorage.getLog().clear();

    String input = "red-component img img-red\n" +
            "green-component img img-green\n" +
            "blue-component img img-blue\n" +
            "value-component img img-value\n" +
            "intensity-component img img-intensity\n" +
            "luma-component img img-luma\n";

    controller = new ImageControllerImpl(new StringReader(input), output, mockStorage);
    controller.run();

    List<String> logs = mockStorage.getLog();

    // Each operation: getImage + putImage = 2 calls × 6 operations = 12
    assertEquals(12, logs.size());

    // Verify all outputs were created
    assertTrue(logs.contains("putImage: img-red"));
    assertTrue(logs.contains("putImage: img-green"));
    assertTrue(logs.contains("putImage: img-blue"));
    assertTrue(logs.contains("putImage: img-value"));
    assertTrue(logs.contains("putImage: img-intensity"));
    assertTrue(logs.contains("putImage: img-luma"));
  }

  @Test
  public void testParsingOverwriteExistingImage() {
    mockStorage.putImage("original", testImage);
    mockStorage.putImage("other", new ImageImpl(1, 1, 255));
    mockStorage.getLog().clear();

    String input = "brighten 100 other original\n"; // Overwrite 'original'

    controller = new ImageControllerImpl(new StringReader(input), output, mockStorage);
    controller.run();

    List<String> logs = mockStorage.getLog();

    assertEquals(2, logs.size());
    assertEquals("getImage: other", logs.get(0));
    assertEquals("putImage: original", logs.get(1)); // Overwrites existing
  }

  @Test
  public void testParsingNonExistentSourceImage() {
    mockStorage.getLog().clear();

    String input = "brighten 50 nonexistent result\n";

    controller = new ImageControllerImpl(new StringReader(input), output, mockStorage);
    controller.run();

    List<String> logs = mockStorage.getLog();

    // Should try to get image and fail
    assertEquals(1, logs.size());
    // operation failed, no putImage
    assertEquals("getImage: nonexistent", logs.get(0));

    assertTrue(output.toString().contains("Error"));
    assertTrue(output.toString().contains("No image found"));
  }

  @Test
  public void testSaveCommand() {
    mockStorage.putImage("tosave", testImage);
    mockStorage.getLog().clear();

    String input = "save output.png tosave\n";

    controller = new ImageControllerImpl(new StringReader(input), output, mockStorage);
    controller.run();

    List<String> logs = mockStorage.getLog();

    // Save only reads from database
    assertEquals(1, logs.size());
    assertEquals("getImage: tosave", logs.get(0));
  }


}