package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import model.IImageHistogram;

/**
 * Custom panel to display image histogram.
 */
public class HistogramPanel extends JPanel {
  private IImageHistogram histogram;

  /**
   * Constructor for the histogram panel to display information about the image.
   * The histogram should take in an IImageHistogram model that contains all the data of the image.
   */
  public HistogramPanel() {
    super();
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder("Image Histogram"));
    setMinimumSize(new Dimension(800, 200));
    setPreferredSize(new Dimension(800, 200));
  }

  /**
   * Update the histogram display with new data.
   *
   * @param histogram the histogram data to display.
   */
  public void updateHistogram(IImageHistogram histogram) {
    this.histogram = histogram;
    repaint();
  }

  /**
   * Clear the histogram display.
   */
  public void clearHistogram() {
    this.histogram = null;
    repaint();
  }

  /**
   * Paint Component display the image on screen.
   * It overrides the JFrame implementation of paintComponent
   * to show placeholder message if no image is loaded.
   *
   * @param g the <code>Graphics</code> object to protect.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (histogram == null) {
      // Draw placeholder when no histogram data
      g.setColor(Color.GRAY);
      FontMetrics fm = g.getFontMetrics();
      String message = "No histogram data available";
      int x = (getWidth() - fm.stringWidth(message)) / 2;
      int y = getHeight() / 2;
      g.drawString(message, x, y);
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    drawHistogramChart(g2d);
  }

  /**
   * Draw the histogram chart with all four components.
   *
   * @param g2d the Graphics2D object to draw with.
   */
  private void drawHistogramChart(Graphics2D g2d) {
    int width = getWidth();
    int height = getHeight();
    int margin = 40;
    int chartWidth = width - 2 * margin;
    int chartHeight = height - 2 * margin - 40; // Space for legend

    // Draw axes
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(1.0f));
    // X-axis
    g2d.drawLine(margin, height - margin - 40, width - margin, height - margin - 40);
    g2d.drawLine(margin, margin, margin, height - margin - 40); // Y-axis

    // Draw axis labels
    g2d.drawString("0", margin - 15, height - margin - 35);
    g2d.drawString("255", width - margin - 15, height - margin - 35);
    g2d.drawString("Value", width / 2 - 15, height - 10);

    // Rotate and draw Y-axis label
    Graphics2D g2dCopy = (Graphics2D) g2d.create();
    g2dCopy.rotate(-Math.PI / 2);
    g2dCopy.drawString("Frequency", -height / 2 - 30, 15);
    g2dCopy.dispose();

    // Get histogram data
    int[] redHist = histogram.getRedHistogram();
    int[] greenHist = histogram.getGreenHistogram();
    int[] blueHist = histogram.getBlueHistogram();
    int[] intensityHist = histogram.getIntensityHistogram();

    // Find max value for scaling
    int maxValue = histogram.getOverallMaxCount();

    if (maxValue > 0) {
      // Draw histogram lines with respective colors
      g2d.setStroke(new BasicStroke(2.0f));

      drawHistogramLine(g2d, redHist, Color.RED, margin, height - margin - 40,
              chartWidth, chartHeight, maxValue);
      drawHistogramLine(g2d, greenHist, Color.GREEN, margin, height - margin - 40,
              chartWidth, chartHeight, maxValue);
      drawHistogramLine(g2d, blueHist, Color.BLUE, margin, height - margin - 40,
              chartWidth, chartHeight, maxValue);
      drawHistogramLine(g2d, intensityHist, Color.BLACK, margin, height - margin - 40,
              chartWidth, chartHeight, maxValue);
    }

    // Draw legend
    drawLegend(g2d, margin, height - 30);
  }

  /**
   * Draw a single histogram line.
   *
   * @param g2d         the graphics context.
   * @param histogram   the histogram data array.
   * @param color       the color to draw with.
   * @param startX      the starting X coordinate.
   * @param startY      the starting Y coordinate (bottom of chart).
   * @param chartWidth  the width of the chart area.
   * @param chartHeight the height of the chart area.
   * @param maxValue    the maximum value for scaling.
   */
  private void drawHistogramLine(Graphics2D g2d, int[] histogram, Color color,
                                 int startX, int startY,
                                 int chartWidth, int chartHeight, int maxValue) {
    g2d.setColor(color);

    for (int i = 0; i < 255; i++) {
      // Calculate X coordinate for current point (i)
      int x1 = startX + (i * chartWidth) / 256;
      // Calculate X coordinate for next point (i+1)
      int x2 = startX + ((i + 1) * chartWidth) / 256;
      // Calculate Y coordinate for current histogram value
      int y1 = startY - (histogram[i] * chartHeight) / maxValue;
      // Calculate Y coordinate for next histogram value
      int y2 = startY - (histogram[i + 1] * chartHeight) / maxValue;

      // Draw the line from x1 -> x2 and y1 -> y2
      g2d.drawLine(x1, y1, x2, y2);
    }
  }

  /**
   * Draw the legend for the histogram.
   *
   * @param g2d the graphics context.
   * @param x   the starting X coordinate.
   * @param y   the starting Y coordinate.
   */
  private void drawLegend(Graphics2D g2d, int x, int y) {
    String[] labels = {"Red", "Green", "Blue", "Intensity"};
    Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.BLACK};

    g2d.setStroke(new BasicStroke(3.0f));
    for (int i = 0; i < labels.length; i++) {
      int legendX = x + i * 100;
      g2d.setColor(colors[i]);
      g2d.drawLine(legendX, y, legendX + 20, y);
      g2d.setColor(Color.BLACK);
      g2d.drawString(labels[i], legendX + 25, y + 4);
    }
  }
}
