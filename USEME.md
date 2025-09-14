# Image Manipulation and Enhancement App

## Running the program

The program can be run from the main class. You are able to run the program in 3 ways:

- Run the program without any arguments will open a GUI where you can load, manipulate, and save the
  image. `java Main`
- Run the main function with a `-file [script location]` or `[script location]`. The program will
  looks for a script from commands and uses the controller to parse it. You can run the script using
  either:
    - `java Main mycommands.txt`
    - or `java Main -file mycommands.txt` where `mycommands.txt` is the script file that you
      need to put in the project root folder.
- Run the main function with a `-test` argument. Use the run button in the IntelliJ or whatever IDE
  you
  use and write the commands. If you load, edit, and save a file. The image will be found in the
  location you saved once you quit out of the console. To quit type `quit`, or `exit`, or `q`.
  `java Main -test`.

## Using the GUI

1. Loading Images:

- Click "Load Image" button
- Select image file (PPM, JPEG, PNG, BMP supported)
- Enter a name for the image
- Image appears in display area and loaded images list

2. Viewing Images:

- Click any image name in the "Loaded Images" list to view it
- Histogram updates automatically
- Use scrollbars if image is larger than display area

3. Processing Images:

- Select an image from the loaded images list
- Choose an operation (brightness, grayscale components)
- For brightness: adjust slider and click "Apply Brightness"
- For grayscale: click the desired component button
- New processed image is added to the list and displayed

4. Saving Images:

- Select the image you want to save from the loaded images list
- Click "Save Image"
- Choose output format (PPM, JPEG, PNG, BMP) from the dialog
- Select save location in file dialog
- File extension is automatically added/corrected

5. Deleting Images:

- Select image from loaded images list
- Click "Delete Image"
- Confirm deletion in dialog

### Available file formats

- PPM - Portable Pixmap Format (text-based, exact quality)
- JPEG - Joint Photographic Experts Group (lossy compression, smaller files)
- PNG - Portable Network Graphics (lossless compression, good for graphics)
- BMP - Windows Bitmap (uncompressed, largest files)

## Available Commands for Interactive Mode

load - Load an image from file. `load image-path image-name`
Example:

```
load images/koala.ppm koala
load photos/sunset.jpg sunset-photo
load graphics/logo.png company-logo

Supported formats: PPM,JPEG/JPG,PNG,BMP
```

save - Save an image to file.
`save image-path image-name`
Example:

```
save output/koala-bright.ppm koala-bright
save results/edited.jpg my-image
save final/logo.png processed-logo
```

brighten - Brighten or darken an image. `brighten value source-name dest-name`
Examples:

```
brighten 50 koala koala-bright # Brighten by 50
brighten -30 koala koala-dark # Darken by 30
brighten 100 sunset sunset-bright # Brighten by 100
brighten -100 sunset sunset-dark # Darken by 100

Note: Positive values brighten, negative values darken
```

red-component - Extract red channel as grayscale. `red-component source-name dest-name`
Examples:

```
red-component koala koala-red
red-component portrait portrait-red-only
```

green-component - Extract green channel as grayscale. `green-component source-name dest-name`.
Examples:

```
green-component koala koala-green
green-component landscape landscape-green-only
```

blue-component - Extract blue channel as grayscale. `blue-component source-name dest-name`.
Examples:

```
blue-component koala koala-blue
blue-component ocean ocean-blue-only
```

value-component - Create grayscale using maximum RGB value. `value-component source-name dest-name`
Examples:

```
value-component koala koala-value
value-component photo photo-value-grayscale
```

intensity-component - Create grayscale using average RGB value.
`intensity-component source-name dest-name`
Examples:

```
intensity-component koala koala-intensity
intensity-component portrait portrait-intensity-grayscale
```

luma-component - Create grayscale using weighted sum. `luma-component source-name dest-name`
Examples:

```
luma-component koala koala-luma
luma-component photo photo-luma-grayscale
```

## Example Scripts

Basic Image Editing:

```
load images/photo.jpg myphoto
brighten 30 myphoto myphoto-bright
save output/bright-photo.jpg myphoto-bright
```

Greyscale:

```
load images/colorful.png image
red-component image image-red
green-component image image-green
blue-component image image-blue
save output/red.png image-red
save output/green.png image-green
save output/blue.png image-blue
```

Script Example: Create a `command.txt` file and put this in the file:

```
# Load and process koala image
load images/koala.ppm koala

# Create brightened and darkened versions
brighten 50 koala koala-bright
brighten -50 koala koala-dark

# Create grayscale versions
value-component koala koala-grayscale
luma-component koala koala-bw

# Save all results
save output/koala-bright.png koala-bright
save output/koala-dark.png koala-dark
save output/koala-grayscale.jpg koala-grayscale
save output/koala-bw.bmp koala-bw
```