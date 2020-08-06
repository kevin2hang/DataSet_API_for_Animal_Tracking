import processing.core.PApplet;

import java.util.ArrayList;

public class MouseTracker implements PixelFilter {
    private static final int MAX_FRAMES = 4500;
    private static final int radius = 207;
    private int xcenter = 308;
    private int ycenter = 234;
    private static final int fps = 25;
    private static final double cmPerPixel = 1 / 5.316;
    private double mouseCenterX, mouseCenterY;
    private ArrayList<Point> positions = new ArrayList<>();
    DataSet dataset;
    int frameCount = 0;
    private ThresholdFilter thresholdFilter = new ThresholdFilter();
    private MatrixConvolution boxBlur = new MatrixConvolution();

    public MouseTracker() {
        dataset = new DataSet(fps, cmPerPixel, positions);
    }

    @Override
    public DImage processImage(DImage img) {
        frameCount++;

        if (frameCount < MAX_FRAMES) {
            boxBlur.processImage(img);
            thresholdFilter.processImage(img);
            boxBlur.processImage(img);
            thresholdFilter.increaseThreshold(5);
            thresholdFilter.processImage(img);
            boxBlur.processImage(img);
            thresholdFilter.increaseThreshold(5);
            thresholdFilter.processImage(img);
            thresholdFilter.increaseThreshold(-10);

            short[][] pixels = img.getBWPixelGrid();
            for (int r = 0; r < pixels.length; r++) {
                for (int c = 0; c < pixels[r].length; c++) {
                    if (distanceFrom(xcenter, ycenter, c, r) > radius) pixels[r][c] = 255;
                }
            }
            img.setPixels(pixels);

            double xSum = 0;
            double ySum = 0;
            double numOfMousePixels = 0;
            for (int r = 0; r < pixels.length; r++) {
                for (int c = 0; c < pixels[r].length; c++) {
                    if (pixels[r][c] == 0) {
                        xSum += c;
                        ySum += r;
                        numOfMousePixels++;
                    }
                }
            }
            mouseCenterX = (xSum) / (numOfMousePixels);
            mouseCenterY = ySum / numOfMousePixels;
            positions.add(new Point((int) mouseCenterY, (int) mouseCenterX));


        } else if (frameCount == MAX_FRAMES) {
            displayInfo(dataset);
            outputCSVData(dataset);
        }

        return img;
    }

    private void displayInfo(DataSet dataset) {

    }

    private void outputCSVData(DataSet dataset) {
        dataset.writeDataToFile("Files/ZHANG_KEVIN.csv");
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        // TODO:  If you want, draw the trail behind the mouse.
        window.fill(100, 255, 100);
        window.ellipse((int) mouseCenterX, (int) mouseCenterY, 5, 5);
        if (positions.size() >= 2) {
            window.stroke(100, 255, 100);
            for (int i = 0; i < positions.size() - 1; i++) {
                Point pos1 = positions.get(i);
                Point pos2 = positions.get(i + 1);
                window.line(pos1.getCol(), pos1.getRow(), pos2.getCol(), pos2.getRow());
            }
        }
    }

    public double distanceFrom(double x, double y, double x1, double y1) {
        double xdiff = (x1 - x);
        double ydiff = y1 - y;
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }


}

