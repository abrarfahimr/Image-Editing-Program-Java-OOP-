package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Custom drawing panel for displaying scrollable images.
 */
class DrawingPanel extends JPanel {
  private BufferedImage image;

  /**
   * Constructor for the drawing panel.
   */
  public DrawingPanel() {
    super();
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(800, 800));
  }

  /**
   * Draw an image on this panel.
   *
   * @param image the BufferedImage to draw.
   * @throws IllegalArgumentException if image is null.
   */
  public void drawImage(BufferedImage image) {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    this.image = image;

    // Update panel size to match image size for proper scrolling
    setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

    // Call the paintComponent method to draw the image on screen
    revalidate();
    repaint();
  }

  /**
   * Clear the current image.
   */
  public void clearImage() {
    this.image = null;
    setPreferredSize(new Dimension(800, 800));
    revalidate();
    repaint();
  }

  /**
   * Paint Component display the image on screen.
   * It overrides the JFrame implementation of paintComponent
   * to show placeholder image is no image is loaded.
   *
   * @param g the <code>Graphics</code> object to protect.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (this.image != null) {
      g.drawImage(this.image, 0, 0, this);
    } else {
      // Draw placeholder text when no image is loaded
      g.setColor(Color.GRAY);
      FontMetrics fm = g.getFontMetrics();
      String message = "No image loaded";
      int x = (getWidth() - fm.stringWidth(message)) / 2;
      int y = getHeight() / 2;
      g.drawString(message, x, y);
    }
  }
}