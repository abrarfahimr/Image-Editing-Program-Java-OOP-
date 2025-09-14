package model;

import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import operations.BlueGreyScaleOperation;
import operations.BrightenOperation;
import operations.GreenGreyScaleOperation;
import operations.IOperation;
import operations.IntensityGreyScaleOperation;
import operations.LumaGreyScaleOperation;
import operations.RedGreyScaleOperation;
import operations.ValueGreyScaleOperation;
import view.reader.IViewReader;
import view.reader.PPMReader;
import view.writer.IWriter;
import view.writer.PPMWriter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Extensive test cases to test all the Interfaces and implementation.
 * It contains test for all public methods of each interface, edge cases such as null value,
 * out of range argument exception, etc.
 * The last few test cases contains test to generate image in the /res folder.
 */
public class IImageTest {

  @Before
  public void setUp() throws Exception {
    IImage testImage = new ImageImpl(2, 2, 255);
    testImage.setPixel(0, 0, 100, 150, 200);
    testImage.setPixel(1, 0, 50, 100, 150);
    testImage.setPixel(0, 1, 200, 100, 50);
    testImage.setPixel(1, 1, 0, 0, 0);
  }

  /**
   * Test Constructors.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testImageConstructorNegativeWidth() {
    new ImageImpl(-1, 10, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImageConstructorNegativeHeight() {
    new ImageImpl(10, -1, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImageConstructorZeroWidth() {
    new ImageImpl(0, 10, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImageConstructorZeroHeight() {
    new ImageImpl(10, 0, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImageConstructorNegativeMaxValue() {
    new ImageImpl(10, 10, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImageConstructorZeroMaxValue() {
    new ImageImpl(10, 10, 0);
  }

  @Test
  public void testImageConstructorValid() {
    IImage image = new ImageImpl(5, 3, 255);
    assertEquals(5, image.getWidth());
    assertEquals(3, image.getHeight());
    assertEquals(255, image.getMaxValue());
  }

  @Test
  public void testSetGetPixelValid() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, 100, 150, 200);
    assertArrayEquals(new int[]{100, 150, 200}, image.getPixel(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelNegativeX() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(-1, 1, 100, 100, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelNegativeY() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, -1, 100, 100, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelXOutOfBounds() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(3, 1, 100, 100, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelYOutOfBounds() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 3, 100, 100, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelNegativeRed() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, -1, 100, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelNegativeGreen() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, 100, -1, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelNegativeBlue() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, 100, 100, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelRedAboveMax() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, 256, 100, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelGreenAboveMax() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, 100, 256, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPixelBlueAboveMax() {
    IImage image = new ImageImpl(3, 3, 255);
    image.setPixel(1, 1, 100, 100, 256);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelNegativeX() {
    IImage image = new ImageImpl(3, 3, 255);
    image.getPixel(-1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelNegativeY() {
    IImage image = new ImageImpl(3, 3, 255);
    image.getPixel(1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelXOutOfBounds() {
    IImage image = new ImageImpl(3, 3, 255);
    image.getPixel(3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelYOutOfBounds() {
    IImage image = new ImageImpl(3, 3, 255);
    image.getPixel(1, 3);
  }

  @Test
  public void testSetPixelOnMinMaxValues() {
    IImage image = new ImageImpl(2, 2, 255);
    // Set min values
    image.setPixel(0, 0, 0, 0, 0);
    assertArrayEquals(new int[]{0, 0, 0}, image.getPixel(0, 0));

    // Set max values
    image.setPixel(1, 1, 255, 255, 255);
    assertArrayEquals(new int[]{255, 255, 255}, image.getPixel(1, 1));
  }

  /**
   * Test PPMReader.
   */
  @Test(expected = IOException.class)
  public void testPPMReaderInvalidMagicNumber() throws IOException {
    StringReader reader = new StringReader("P6\n2 2\n255\n 0 0 0 0 0 0\n 0 0 0 0 0 0\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderNegativeWidth() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader("P3\n-2 2\n255\n 0 0 0 0 0 0\n 0 0 0 0 0 0\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderNegativeHeight() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader("P3\n2 -2\n255\n 0 0 0 0 0 0\n 0 0 0 0 0 0\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderZeroWidth() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader("P3\n0 2\n255\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderZeroHeight() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader("P3\n2 0\n255\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderInvalidMaxValue() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader("P3\n2 2\n0\n 0 0 0 0 0 0\n 0 0 0 0 0 0\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IOException.class)
  public void testPPMReaderNotEnoughData() throws IOException {
    StringReader reader = new StringReader("P3\n2 2\n255\n 10 10 10 20 20\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderInvalidPixelValue() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader(
            "P3\n2 2\n255\n 10 10 10 20 20 20\n 30 30 30 256 40 40\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPPMReaderNegativePixelValue() throws IllegalArgumentException, IOException {
    StringReader reader = new StringReader(
            "P3\n2 2\n255\n -10 10 10 20 20 20\n 30 30 30 40 40 40\n");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  @Test(expected = IOException.class)
  public void testPPMReaderEmptyFile() throws IOException {
    StringReader reader = new StringReader("");
    IViewReader ppmReader = new PPMReader(reader);
    ppmReader.read();
  }

  /**
   * PPMWriter tests.
   */

  @Test(expected = IllegalArgumentException.class)
  public void testPPMWriterNullAppendable() {
    new PPMWriter(null);
  }

  @Test(expected = NullPointerException.class)
  public void testPPMWriterNullImage() throws NullPointerException, IOException {
    StringBuilder builder = new StringBuilder();
    IWriter writer = new PPMWriter(builder);
    writer.write(null);
  }

  @Test
  public void testPPMWriterSinglePixel() throws IOException {
    IImage image = new ImageImpl(1, 1, 255);
    image.setPixel(0, 0, 128, 64, 32);

    StringBuilder builder = new StringBuilder();
    IWriter writer = new PPMWriter(builder);
    writer.write(image);

    assertEquals("P3\n1 1\n255\n 128 64 32\n", builder.toString());
  }

  @Test
  public void testReadAndWrite() throws IOException {
    StringReader readable = new StringReader(
            "P3\n2 2\n255\n 10 10 10 20 20 20\n 30 30 30 40 40 40\n");
    IViewReader reader = new PPMReader(readable);

    IImage image = reader.read();

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(image);

    assertEquals(
            "P3\n2 2\n255\n 10 10 10 20 20 20\n 30 30 30 40 40 40\n", builder.toString());
  }

  /**
   * Brighten Operation Tests.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBrightenOperationNullImage() {
    IOperation brighten = new BrightenOperation(50);
    brighten.apply(null);
  }

  @Test
  public void testBrightenImage() throws IOException {
    StringReader readable = new StringReader(
            "P3\n2 2\n255\n 10 10 10 20 20 20\n 30 30 30 40 40 40\n");
    IViewReader reader = new PPMReader(readable);

    IImage image = reader.read();

    IOperation brighten = new BrightenOperation(10);
    IImage brightenedImage = brighten.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(brightenedImage);

    assertEquals(
            "P3\n2 2\n255\n 20 20 20 30 30 30\n 40 40 40 50 50 50\n", builder.toString());
  }

  @Test
  public void testBrightenOperationClamping() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 200 200 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation brighten = new BrightenOperation(100);
    IImage brightenedImage = brighten.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(brightenedImage);

    assertEquals(
            "P3\n2 1\n255\n 255 255 255 255 100 228\n", builder.toString());
  }

  @Test
  public void testBrightenOperationLargeIncrement() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 200 200 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation brighten = new BrightenOperation(1000);
    IImage brightenedImage = brighten.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(brightenedImage);

    assertEquals(
            "P3\n2 1\n255\n 255 255 255 255 255 255\n", builder.toString());
  }

  /**
   * Darken Operation Tests.
   */
  @Test
  public void testDarkenImage() throws IOException {
    StringReader readable = new StringReader(
            "P3\n2 2\n255\n 10 10 10 20 20 20\n 30 30 30 40 40 40\n");
    IViewReader reader = new PPMReader(readable);

    IImage image = reader.read();

    IOperation brighten = new BrightenOperation(-10);
    IImage brightenedImage = brighten.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(brightenedImage);

    assertEquals("P3\n2 2\n255\n 0 0 0 10 10 10\n 20 20 20 30 30 30\n", builder.toString());
  }

  @Test
  public void testDarkenOperationClamping() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 50 50 50 0 255 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation brighten = new BrightenOperation(-100);
    IImage brightenedImage = brighten.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(brightenedImage);

    assertEquals(
            "P3\n2 1\n255\n 0 0 0 0 155 28\n", builder.toString());
  }

  @Test
  public void testDarkenOperationLargeIncrement() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 200 200 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation brighten = new BrightenOperation(-1000);
    IImage brightenedImage = brighten.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(brightenedImage);

    assertEquals(
            "P3\n2 1\n255\n 0 0 0 0 0 0\n", builder.toString());
  }

  /**
   * Greyscale Operation tests.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRedGrayscaleNullImage() {
    IOperation grayscale = new RedGreyScaleOperation();
    grayscale.apply(null);
  }

  @Test
  public void testRedGrayscaleOperation() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 100 150 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new RedGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n2 1\n255\n 100 100 100 255 255 255\n", builder.toString());
  }

  @Test
  public void testGreenGrayscaleOperation() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 100 150 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new GreenGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n2 1\n255\n 150 150 150 0 0 0\n", builder.toString());
  }

  @Test
  public void testBlueGrayscaleOperation() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 100 150 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new BlueGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n2 1\n255\n 200 200 200 128 128 128\n", builder.toString());
  }

  @Test
  public void testIntensityGrayscaleOperation() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 100 150 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new IntensityGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n2 1\n255\n 150 150 150 127 127 127\n", builder.toString());
  }

  @Test
  public void testValueGrayscaleOperation() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 100 150 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new ValueGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n2 1\n255\n 200 200 200 255 255 255\n", builder.toString());
  }

  @Test
  public void testLumaGrayscaleOperation() throws IOException {
    StringReader readable = new StringReader("P3\n2 1\n255\n 100 150 200 255 0 128\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new LumaGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n2 1\n255\n 142 142 142 63 63 63\n", builder.toString());
  }

  @Test
  public void testGrayscaleOnBlackPixel() throws IOException {
    StringReader readable = new StringReader("P3\n1 1\n255\n 0 0 0\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new LumaGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    assertEquals("P3\n1 1\n255\n 0 0 0\n", builder.toString());
  }

  @Test
  public void testGrayscaleOnWhitePixel() throws IOException {
    StringReader readable = new StringReader("P3\n1 1\n255\n 255 255 255\n");
    IViewReader reader = new PPMReader(readable);
    IImage image = reader.read();

    IOperation grayscale = new LumaGreyScaleOperation();
    IImage greyscaleImage = grayscale.apply(image);

    StringBuilder builder = new StringBuilder();
    IWriter ppmWriter = new PPMWriter(builder);

    ppmWriter.write(greyscaleImage);

    // Luma Operation rounds down the values due to casting
    assertEquals("P3\n1 1\n255\n 254 254 254\n", builder.toString());
  }

  /**
   * Tests to generate Images.
   *
   * @throws IOException throws IOException if failed to read/write to file.
   */
  @Test
  public void generateBrightenImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-brighten50.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation brighten = new BrightenOperation(50);
      IImage brightenedImage = brighten.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(brightenedImage);

      assertEquals(image.getWidth(), brightenedImage.getWidth());
      assertEquals(image.getHeight(), brightenedImage.getHeight());
      assertEquals(image.getMaxValue(), brightenedImage.getMaxValue());

    }
  }

  @Test
  public void generateDarkenImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-darken50.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation darken = new BrightenOperation(-50);
      IImage darkenImage = darken.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(darkenImage);

      assertEquals(image.getWidth(), darkenImage.getWidth());
      assertEquals(image.getHeight(), darkenImage.getHeight());
      assertEquals(image.getMaxValue(), darkenImage.getMaxValue());
    }
  }

  @Test
  public void generateGreyScaleRedImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-greyscaleRed.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation greyscaleRed = new RedGreyScaleOperation();
      IImage greyscaleRedImage = greyscaleRed.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(greyscaleRedImage);

      assertEquals(image.getWidth(), greyscaleRedImage.getWidth());
      assertEquals(image.getHeight(), greyscaleRedImage.getHeight());
      assertEquals(image.getMaxValue(), greyscaleRedImage.getMaxValue());
    }
  }

  @Test
  public void generateGreyScaleGreenImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-greyscaleGreen.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation greyscaleGreen = new GreenGreyScaleOperation();
      IImage greyscaleGreenImage = greyscaleGreen.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(greyscaleGreenImage);

      assertEquals(image.getWidth(), greyscaleGreenImage.getWidth());
      assertEquals(image.getHeight(), greyscaleGreenImage.getHeight());
      assertEquals(image.getMaxValue(), greyscaleGreenImage.getMaxValue());
    }
  }

  @Test
  public void generateGreyScaleBlueImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-greyscaleBlue.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation greyscaleBlue = new BlueGreyScaleOperation();
      IImage greyscaleBlueImage = greyscaleBlue.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(greyscaleBlueImage);

      assertEquals(image.getWidth(), greyscaleBlueImage.getWidth());
      assertEquals(image.getHeight(), greyscaleBlueImage.getHeight());
      assertEquals(image.getMaxValue(), greyscaleBlueImage.getMaxValue());
    }
  }

  @Test
  public void generateGreyScaleIntensityImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-greyscaleIntensity.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation greyscaleIntensity = new IntensityGreyScaleOperation();
      IImage greyscaleIntensityImage = greyscaleIntensity.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(greyscaleIntensityImage);

      assertEquals(image.getWidth(), greyscaleIntensityImage.getWidth());
      assertEquals(image.getHeight(), greyscaleIntensityImage.getHeight());
      assertEquals(image.getMaxValue(), greyscaleIntensityImage.getMaxValue());
    }
  }

  @Test
  public void generateGreyScaleValueImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-greyscaleValue.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation greyscaleValue = new ValueGreyScaleOperation();
      IImage greyscaleValueImage = greyscaleValue.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(greyscaleValueImage);

      assertEquals(image.getWidth(), greyscaleValueImage.getWidth());
      assertEquals(image.getHeight(), greyscaleValueImage.getHeight());
      assertEquals(image.getMaxValue(), greyscaleValueImage.getMaxValue());
    }
  }

  @Test
  public void generateGreyScaleLumaImage() throws IOException {
    try (FileReader fileReader = new FileReader("./res/my_cat.ppm");
         FileWriter fileWriter = new FileWriter("./res/my_cat-greyscaleLuma.ppm")) {

      IViewReader imageReader = new PPMReader(fileReader);
      IImage image = imageReader.read();

      IOperation greyscaleLuma = new LumaGreyScaleOperation();
      IImage greyscaleLumaImage = greyscaleLuma.apply(image);

      IWriter imageWriter = new PPMWriter(fileWriter);
      imageWriter.write(greyscaleLumaImage);

      assertEquals(image.getWidth(), greyscaleLumaImage.getWidth());
      assertEquals(image.getHeight(), greyscaleLumaImage.getHeight());
      assertEquals(image.getMaxValue(), greyscaleLumaImage.getMaxValue());
    }
  }
}