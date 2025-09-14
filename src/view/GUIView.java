package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.IImageHistogram;

/**
 * GUI view for the image processing application using Java Swing.
 * This class handles all user interface interactions and emits high-level
 * events to registered listeners.
 */
public class GUIView extends JFrame implements ActionListener {

  // Main display components
  private final DrawingPanel drawingPanel;
  private final HistogramPanel histogramPanel;
  private final JScrollPane imageScrollPane;

  // Control panels
  private final JPanel eastPanel;
  private final JPanel southPanel;
  private final JPanel operationsPanel;

  // Image list management
  private final JList<String> imageList;
  private final JLabel imagesLabel;
  private String imageName;
  private final DefaultListModel<String> listModel;

  // Control buttons
  private final JButton loadImageButton;
  private final JButton saveImageButton;
  private final JButton deleteImageButton;
  private final JButton brightenButton;
  private final JButton redComponentButton;
  private final JButton greenComponentButton;
  private final JButton blueComponentButton;
  private final JButton valueComponentButton;
  private final JButton intensityComponentButton;
  private final JButton lumaComponentButton;

  // Input components
  private final JSlider brightnessSlider;
  private final JLabel brightnessLabel;

  // View listeners
  private final List<ImageViewListener> viewListeners;

  /**
   * Constructor to initialize the GUI view.
   */
  public GUIView() {
    super("Graphical Image Manipulation and Enhancement");

    // Initialize collections
    this.viewListeners = new ArrayList<>();
    this.listModel = new DefaultListModel<>();

    // Initialize main components
    this.drawingPanel = new DrawingPanel();
    this.histogramPanel = new HistogramPanel();
    this.imageList = new JList<>(this.listModel);
    this.imageName = "";
    this.imagesLabel = new JLabel("Images");

    // Initialize buttons
    this.loadImageButton = new JButton("Load Image");
    this.saveImageButton = new JButton("Save Image");
    this.deleteImageButton = new JButton("Delete Image");
    this.brightenButton = new JButton("Brighten");
    this.redComponentButton = new JButton("Red Component");
    this.greenComponentButton = new JButton("Green Component");
    this.blueComponentButton = new JButton("Blue Component");
    this.valueComponentButton = new JButton("Value Component");
    this.intensityComponentButton = new JButton("Intensity Component");
    this.lumaComponentButton = new JButton("Luma Component");

    // Initialize input components
    this.brightnessSlider = new JSlider(-100, 100, 0);
    this.brightnessLabel = new JLabel("Brightness: 0");

    // Create panels
    this.operationsPanel = createOperationsPanel();
    this.eastPanel = createEastPanel();
    this.southPanel = createSouthPanel();
    this.imageScrollPane = new JScrollPane(this.drawingPanel);

    // Setup frame
    setupFrame();
    setupActionListeners();
  }

  /**
   * Set up the main frame properties and layout.
   */
  private void setupFrame() {
    setSize(new Dimension(1200, 800));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Add components to frame
    add(this.imageScrollPane, BorderLayout.CENTER);
    add(this.eastPanel, BorderLayout.EAST);
    add(this.southPanel, BorderLayout.SOUTH);
  }

  /**
   * Setup action listeners and action commands for all buttons.
   */
  private void setupActionListeners() {
    // File operations
    this.loadImageButton.addActionListener(this);
    this.loadImageButton.setActionCommand("loadImage");

    this.saveImageButton.addActionListener(this);
    this.saveImageButton.setActionCommand("saveImage");

    this.deleteImageButton.addActionListener(this);
    this.deleteImageButton.setActionCommand("deleteImage");

    // Image operations
    this.brightenButton.addActionListener(this);
    this.brightenButton.setActionCommand("brighten");

    this.redComponentButton.addActionListener(this);
    this.redComponentButton.setActionCommand("redComponent");

    this.greenComponentButton.addActionListener(this);
    this.greenComponentButton.setActionCommand("greenComponent");

    this.blueComponentButton.addActionListener(this);
    this.blueComponentButton.setActionCommand("blueComponent");

    this.valueComponentButton.addActionListener(this);
    this.valueComponentButton.setActionCommand("valueComponent");

    this.intensityComponentButton.addActionListener(this);
    this.intensityComponentButton.setActionCommand("intensityComponent");

    this.lumaComponentButton.addActionListener(this);
    this.lumaComponentButton.setActionCommand("lumaComponent");

    // Brightness slider
    this.brightnessSlider.addChangeListener(e -> {
      if (!brightnessSlider.getValueIsAdjusting()) {
        int value = brightnessSlider.getValue();
        brightnessLabel.setText("Brightness: " + value);
      }
    });

    // Image list selection listener
    imageList.addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) { // Only handle final selection
        String selectedImageName = imageList.getSelectedValue();
        if (selectedImageName != null) {
          // Notify listeners to display the selected image
          for (ImageViewListener listener : viewListeners) {
            listener.displaySelectedImage(selectedImageName);
          }
        }
      }
    });

    // Disable operation buttons initially
    setOperationButtonsEnabled(false);
  }

  /**
   * Create the operations panel with all image manipulation buttons.
   */
  private JPanel createOperationsPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Image Operations"));

    // File operations
    panel.add(loadImageButton);
    panel.add(saveImageButton);
    panel.add(deleteImageButton);

    // Brightness controls
    JPanel brightnessPanel = new JPanel();
    brightnessPanel.setLayout(new BoxLayout(brightnessPanel, BoxLayout.Y_AXIS));
    brightnessPanel.setBorder(BorderFactory.createTitledBorder("Brightness"));

    brightnessSlider.setMajorTickSpacing(50);
    brightnessSlider.setMinorTickSpacing(25);
    brightnessSlider.setPaintTicks(true);
    brightnessSlider.setPaintLabels(true);

    brightnessPanel.add(brightnessLabel);
    brightnessPanel.add(brightnessSlider);
    brightnessPanel.add(brightenButton);

    panel.add(brightnessPanel);

    // Grayscale operations
    JPanel grayscalePanel = new JPanel();
    grayscalePanel.setLayout(new GridLayout(3, 2, 5, 5));
    grayscalePanel.setBorder(BorderFactory.createTitledBorder("Grayscale Operations"));

    grayscalePanel.add(redComponentButton);
    grayscalePanel.add(greenComponentButton);
    grayscalePanel.add(blueComponentButton);
    grayscalePanel.add(valueComponentButton);
    grayscalePanel.add(intensityComponentButton);
    grayscalePanel.add(lumaComponentButton);

    panel.add(grayscalePanel);

    return panel;
  }

  /**
   * Create the east panel with image list.
   */
  private JPanel createEastPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(Color.LIGHT_GRAY);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createTitledBorder("Loaded Images"));
    panel.setPreferredSize(new Dimension(200, 0));

    panel.add(this.imagesLabel);

    // Setup image list
    imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane listScrollPane = new JScrollPane(imageList);
    listScrollPane.setPreferredSize(new Dimension(180, 150));
    panel.add(listScrollPane);

    return panel;
  }

  /**
   * Create the south panel with operations and histogram.
   */
  private JPanel createSouthPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    panel.add(this.operationsPanel);
    panel.add(this.histogramPanel);

    return panel;
  }

  /**
   * Add an image ID to the list of loaded images.
   *
   * @param imageId the ID of the image to add.
   */
  public void addImageId(String imageId) {
    if (imageId != null && !imageId.trim().isEmpty()) {
      listModel.add(0, imageId);
      imageList.setSelectedIndex(0); // Select the newly added image
    }
  }

  /**
   * Remove an image name from the list.
   *
   * @param imageName the name of the image to remove.
   */
  public void removeImageName(String imageName) {
    listModel.removeElement(imageName);
  }

  /**
   * Get the currently selected image name from the list.
   *
   * @return the selected image name, or null if none selected.
   */
  public String getSelectedImageName() {
    return imageList.getSelectedValue();
  }

  /**
   * Update the viewing area with a new image.
   * This method is called by the controller when it has loaded a new image.
   * or an operation has been performed to update what image is being displayed.
   *
   * @param image the BufferedImage to display.
   * @throws IllegalArgumentException if image is null.
   */
  public void updateViewingArea(BufferedImage image) {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    drawingPanel.drawImage(image);
    setOperationButtonsEnabled(true);
  }

  /**
   * Update the histogram display with new histogram data.
   *
   * @param histogram the histogram data to display.
   */
  public void updateHistogram(IImageHistogram histogram) {
    histogramPanel.updateHistogram(histogram);
  }

  /**
   * Clear the image display.
   */
  public void clearImageDisplay() {
    drawingPanel.clearImage();
    histogramPanel.clearHistogram();
    setOperationButtonsEnabled(false);
  }

  /**
   * Show an error message to the user.
   *
   * @param message the error message to display.
   */
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Show an information message to the user.
   *
   * @param message the message to display.
   */
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(
            this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Enable or disable operation buttons.
   *
   * @param enabled true to enable buttons, false to disable.
   */
  private void setOperationButtonsEnabled(boolean enabled) {
    saveImageButton.setEnabled(enabled);
    deleteImageButton.setEnabled(enabled);
    brightenButton.setEnabled(enabled);
    redComponentButton.setEnabled(enabled);
    greenComponentButton.setEnabled(enabled);
    blueComponentButton.setEnabled(enabled);
    valueComponentButton.setEnabled(enabled);
    intensityComponentButton.setEnabled(enabled);
    lumaComponentButton.setEnabled(enabled);
  }

  /**
   * Register a new view listener.
   *
   * @param viewListener the listener to add.
   * @throws IllegalArgumentException if listener is null.
   */
  public void addViewListener(ImageViewListener viewListener) {
    if (viewListener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    this.viewListeners.add(viewListener);
  }

  /**
   * Get the brightness value from the slider.
   *
   * @return the current brightness value.
   */
  private int getBrightnessValue() {
    return brightnessSlider.getValue();
  }

  /**
   * Reset the brightness slider to zero.
   */
  private void resetBrightnessSlider() {
    brightnessSlider.setValue(0);
    brightnessLabel.setText("Brightness: 0");
  }

  /**
   * Handle all button click events.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();

    try {
      switch (command) {
        case "loadImage":
          handleLoadImage();
          break;
        case "saveImage":
          handleSaveImage();
          break;
        case "deleteImage":
          handleDeleteImage();
          break;
        case "brighten":
          handleBrighten();
          break;
        case "redComponent":
          handleGrayscaleOperation("red-component");
          break;
        case "greenComponent":
          handleGrayscaleOperation("green-component");
          break;
        case "blueComponent":
          handleGrayscaleOperation("blue-component");
          break;
        case "valueComponent":
          handleGrayscaleOperation("value-component");
          break;
        case "intensityComponent":
          handleGrayscaleOperation("intensity-component");
          break;
        case "lumaComponent":
          handleGrayscaleOperation("luma-component");
          break;
        default:
          throw new IllegalStateException("Unknown action command: " + command);
      }
    } catch (Exception ex) {
      showError("Error: " + ex.getMessage());
    }
  }

  /**
   * Handle load image button click.
   */
  private void handleLoadImage() {
    JFileChooser fileChooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "ppm", "bmp");
    fileChooser.setFileFilter(filter);

    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String filePath = selectedFile.getAbsolutePath();

      // Get image name from user
      this.imageName = JOptionPane.showInputDialog(this,
              "Enter a name for this image:",
              "Image Name",
              JOptionPane.PLAIN_MESSAGE);

      if (imageName != null && !imageName.trim().isEmpty()) {
        // Notify all listeners
        for (ImageViewListener listener : viewListeners) {
          listener.onLoadImage(filePath, imageName.trim());
        }
      }
    } else if (result == JFileChooser.CANCEL_OPTION) {
      System.out.println("File selection cancelled.");
    } else if (result == JFileChooser.ERROR_OPTION) {
      showError("Error during file selection.");
    }
  }

  /**
   * Handle save image button click.
   */
  private void handleSaveImage() {
    String selectedImageName = getSelectedImageName();
    if (selectedImageName == null) {
      showError("Please select an image to save.");
      return;
    }

    // Let the user choose the image format
    String selectedFormat = showFormatSelectionDialog();
    if (selectedFormat == null) {
      return; // user cancels format selection
    }

    JFileChooser fileChooser = new JFileChooser(".");
    FileNameExtensionFilter filter = createFileFilterForFormat(selectedFormat);
    fileChooser.setFileFilter(filter);
    fileChooser.setAcceptAllFileFilterUsed(false); // Only show the selected format

    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String filePath = selectedFile.getAbsolutePath();

      // Check if the file has correct extension
      filePath = ensureCorrectExtension(filePath, selectedFormat);

      // Notify all listeners
      for (ImageViewListener listener : viewListeners) {
        listener.onSaveImage(filePath, selectedImageName);
      }
    }
  }

  /**
   * Show a dialog for the user to select the image format to save as.
   *
   * @return the selected format (lowercase), or null if cancelled.
   */
  private String showFormatSelectionDialog() {
    String[] formats = {"PPM", "JPEG", "PNG", "BMP"};
    String[] descriptions = {
        "PPM - Portable Pixmap Format (text-based)",
        "JPEG - Joint Photographic Experts Group (lossy compression)",
        "PNG - Portable Network Graphics (lossless compression)",
        "BMP - Windows Bitmap (uncompressed)"
    };

    // Create a custom dialog with format descriptions
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel instructionLabel = new JLabel("Select the image format to save as:");
    instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    panel.add(instructionLabel);

    ButtonGroup group = new ButtonGroup();
    JRadioButton[] radioButtons = new JRadioButton[formats.length];

    for (int i = 0; i < formats.length; i++) {
      radioButtons[i] = new JRadioButton(descriptions[i]);
      radioButtons[i].setActionCommand(formats[i].toLowerCase());
      group.add(radioButtons[i]);
      panel.add(radioButtons[i]);

      // Select PNG as default
      if (formats[i].equals("PNG")) {
        radioButtons[i].setSelected(true);
      }
    }

    int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Select Image Format",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
      // Find which radio button is selected
      for (JRadioButton button : radioButtons) {
        if (button.isSelected()) {
          return button.getActionCommand(); // Returns lowercase format
        }
      }
    }

    return null; // User cancelled or no selection
  }

  /**
   * Create a file filter for the specified format.
   *
   * @param format the image format (lowercase).
   * @return FileNameExtensionFilter for the format.
   */
  private FileNameExtensionFilter createFileFilterForFormat(String format) {
    switch (format) {
      case "ppm":
        return new FileNameExtensionFilter("PPM Files (*.ppm)", "ppm");
      case "jpeg":
        return new FileNameExtensionFilter("JPEG Files (*.jpg, *.jpeg)", "jpg", "jpeg");
      case "png":
        return new FileNameExtensionFilter("PNG Files (*.png)", "png");
      case "bmp":
        return new FileNameExtensionFilter("BMP Files (*.bmp)", "bmp");
      default:
        return new FileNameExtensionFilter(
                "All Supported Images", "ppm", "jpg", "jpeg", "png", "bmp");
    }
  }

  /**
   * Ensure the file path has the correct extension for the selected format.
   *
   * @param filePath the original file path
   * @param format   the selected format (lowercase)
   * @return the file path with correct extension
   */
  private String ensureCorrectExtension(String filePath, String format) {
    String extension;
    switch (format) {
      case "ppm":
        extension = ".ppm";
        break;
      case "jpeg":
        extension = ".jpg";
        break;
      case "png":
        extension = ".png";
        break;
      case "bmp":
        extension = ".bmp";
        break;
      default:
        return filePath; // No change
    }

    // Check if file already has the correct extension
    if (filePath.toLowerCase().endsWith(extension)) {
      return filePath;
    }

    // Remove any existing extension and add the correct one
    int lastDotIndex = filePath.lastIndexOf('.');
    if (lastDotIndex > filePath.lastIndexOf('/')
            && lastDotIndex > filePath.lastIndexOf('\\')) {
      // Has an extension, replace it
      filePath = filePath.substring(0, lastDotIndex);
    }

    return filePath + extension;
  }

  /**
   * Handle delete image button click.
   */
  private void handleDeleteImage() {
    String selectedImageName = getSelectedImageName();
    if (selectedImageName == null) {
      showError("Please select an image to delete.");
      return;
    }

    int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete image: " + selectedImageName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

    if (result == JOptionPane.YES_OPTION) {
      // Notify all listeners
      for (ImageViewListener listener : viewListeners) {
        listener.onDeleteImage(selectedImageName, this.imageName);
      }
    }
  }

  /**
   * Handle brighten button click.
   */
  private void handleBrighten() {
    String selectedImageName = getSelectedImageName();
    if (selectedImageName == null) {
      showError("Please select an image to brighten.");
      return;
    }

    int brightnessValue = getBrightnessValue();

    // Notify all listeners
    for (ImageViewListener listener : viewListeners) {
      listener.onBrightnessOperation(selectedImageName, brightnessValue);
    }

    // Reset slider after operation
    resetBrightnessSlider();
  }

  /**
   * Handle grayscale operation requests.
   *
   * @param operation the type of grayscale operation.
   */
  private void handleGrayscaleOperation(String operation) {
    String selectedImageName = getSelectedImageName();
    if (selectedImageName == null) {
      showError("Please select an image to apply " + operation + ".");
      return;
    }

    // Notify all listeners
    for (ImageViewListener listener : viewListeners) {
      listener.onGrayscaleOperation(selectedImageName, operation);
    }
  }
}