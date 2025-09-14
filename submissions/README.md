## Photo citations

"Picture of my cat" my_cat.ppm, May 10, 2024.

- This is my own photo taken on May 10, 2024.

## Code Design

The code to implement the Image Manipulation program can be found in the `/src` file.
The design of the program follows the SOLID Principles and it divided in the MVC software
architecture pattern:

- **Model**: Contains the interface fro the `IImage` class that builds the image pixel by pixel. The
  RGB component of each pixel is stored in a single array.
    - I used single array to improve memory efficiency and for faster retrieval of data. In order to
      retrieve the correct RGB value of a pixel at a certain coordinate, `getPixelIndex` helper
      method is used.
    - `setPixel` is used by the operation classes to change create a new image with the changes it
      has made to the image.
    - `getPixel` retrieves the r,g,b values of a pixel as an array[] of integers.
    - The model checks whether the width, height, and the maxValues are not less than or equal to 0.
- **Operations**: `IOperation` objects are used to manipulte the images. They are designed using the
  strategy pattern. The operations include: Greyscale based on the red, green, blue, intensity,
  value, and luma of the image. And brighten/darkening the image.
    - An abstract class is created for the Greyscale operations since they share the same code. Only
      the `getValueToUse` helper functions takes the correct value from each operation.
    - Each operation takes into account if image object is null, if the edited image values does not
      go beyond the range (for our test case that would be 0 to 255).
- **View**: The view contains the Read and Write Interface to Read from file/Readable object and
  Write to a file/Appendable object.
    - I have separated the Read and Write implementation in separate folders for future
      implementations when we decided to implement other forms of image types such as jpg and png. (
      separation of concern)
    - The `PPMReader` takes in the data from the ppm file and creates an object of type `IImage`(The
      model implementation)
    - The `PPMWriter` takes the `IImage` model object and writes to the file/Appendable object after
      we make the edits to the image using the `IOperation`objects.

The images are saved in the `res` file after they are edited. All converted images are saved in a
PPM file and are named based on the operation used to changed the image. E.g `my_cat-brighten50.ppm`
is the image that has been brighten using a value of 50. (You can see the changes using Intellij's
built in image preview).

# Changes to design implementation for Assignment 9

The model and operations are run via the controller using the command patter. These are the new
classes that are added:

- `IImageController`: Interface for the controller that has only one operation `run`.
    - Controller Features:
        - Protected HashMap stores command factories
        - Supports comments (lines starting with #)
        - Ignores empty lines
        - Provides detailed error messages
          Works with any Readable input and Appendable output
- The controller uses the command pattern which initializes all the commands needed. All the
  commands are found the in the `commands` folder. Here are the commands implemented.
    - `ICommand`: Interface for all the commands. Has only one operation `run` that runs the
      respective `IOperation` class.
    - `BrightenCommand`: Runs the `BrightenOperation` class and add the new `IImage` to the Image
      Database. If the value is positive, it brightens the image. If it is negative it darkens the
      image. It the value is 0 it just stores the same `IImage` class.
        - **(Note: I have changed the Brighten command to use both a positive and negative value.
          Before it was separated into the BrightenOperation and DarkenOperation but to comply with
          the assignment deliverables, I changed it to a single class)**
    - `AbstractGreyScale`: Abstract class that all other GreyScale class extends from. It abstracts
      the `getGreyScaleOperation` method which gets the correct operation to use in the image.
    - `RedGreyScaleCommand`: For the red-component greyscale operation.
    - `GreenGreyScaleCommand`: For the green-component greyscale operation.
    - `BlueGreyScaleCommand`: For the blue-component greyscale operation.
    - `ValueGreyScaleCommand`: For the value-component greyscale operation.
    - `IntensityGreyScaleCommand`: For the intensity-component greyscale operation.
    - `LumaGreyScaleCommand`: For the luma-component greyscale operation.
- `IImageStorage`: Interface for the image database. It contains all the methods needed to get the
  image and adding an image to it. It stores the image in a `HashMap` with the `name` as the key and
  `IImage` class as the value. The `IImageStorageImpl` is the implementation class of the interface
  that initializes the hashmap and the methods.
    - `addImage()` - adds new image (fails if name exists)
    - `putImage()` - adds or overwrites image
    - `getImage()` - retrieves image by name
    - `hasImage()` - checks if image exists
    - `getAllImages()` - returns unmodifiable view of all images
- `StandardImageReader` and `StandardImageWriter`: Implements the `IViewReader` interface and is
  able to read and write from standard image types such as jpeg, png, and bmp.

# `ProcessImage` Main Function

Main class from where the program starts. You are able to run the program in two ways:

- Run the main function using a script file. The program checks the scripts from commands and uses
  the controller to parse it. You can run the script using either `java ProcessImage mycommands.txt`
  or `java ProcessImage -file mycommands.txt` where `mycommands.txt` is the script file that you
  need to put in the project root folder.
- Run the main function using the console. Use the run button in the IntelliJ or whatever IDE you
  use and write the commands. If you load, edit, and save a file. The image will be found in the
  location you saved once you quit out of the console. To quit type `quit`, or `exit`, or `q`

# Changes to design implementation for Assignment 10

The program now supports a graphical user interface (GUI) built using Java Swing, implementing
proper MVC architecture with high-level event handling. These are the new classes and features
added:

- `GUIView`: The main GUI class extending JFrame that provides an interactive graphical interface
  with
  image display, histogram visualization, and user controls.
    - Image Display Panel (Center): Shows the current image with automatic scrolling support for
      large
      images. It uses a custom `DrawingPanel` that properly handles image rendering and scaling.
    - Image List Panel (Right): Displays all loaded images in a scrollable list on the right side.
      Users can
      click any image name to switch between different loaded or processed images instantly.
    - Control Panels (Bottom): Contains organized sections for file operations (Load, Save,
      Delete), brightness adjustment with slider, and all grayscale operations with dedicated
      buttons.
    - Histogram Panel (Bottom most panel): Real-time histogram display showing red, green, blue, and
      intensity components as line charts. The `HistogramPanel` class automatically updates when
      images are selected or processed.
- `ImageGUIController`: Implements both ImageViewListener and IImageController interfaces to handle
  GUI events and coordinate between the view and model.
    - High-Level Event Handling: Controller responds to meaningful user actions and not button
      actions.
    - Image Processing Integration: Uses existing command pattern classes for all image operations,
      ensuring consistency between GUI and text modes.
    - Automatic Display Updates: After each operation, automatically updates the view with the
      processed image and refreshes the histogram.
- `ImageHistogramImpl`: Implements `IImageHistogram` interface for computing and storing histogram
  data for all color components.
    - Histogram computation is performed in the controller and passed to the view for display,
      following proper MVC principles.
    - Histograms automatically refresh when users switch between images or apply operations.
    - Shows red, green, blue, and intensity histograms simultaneously with different colored lines
      and clear legends.
- Other Integrations:
    - Smart File Extension Handling: Automatically adds or corrects file extensions based on the
      selected format.
    - File Dialog Integration: Uses native file choosers for loading and saving with appropriate
      format filters.
    - Clear status messages and error dialogs inform users of successful operations or issues.

### Changes made in Assignment 10:
- Added a `removeImage` method in `ImageStorageImpl` to delete images from the database.
- Add extra line of code in `ProcessImage` main function to add GUI view.