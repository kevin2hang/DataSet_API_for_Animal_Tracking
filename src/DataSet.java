import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataSet {
    private int fps, radiusOfEnclosure;
    private double averageSpeed, cmPerPxl;

    private ArrayList<Region> regionsOfInterest = new ArrayList<>();
    private ArrayList<Point> mousePositions, mousePositionsFromCSVData;
    private ArrayList<Double> speeds = new ArrayList<Double>();
    private ArrayList<TimeInterval> timeIntervalsInROI = new ArrayList<TimeInterval>();

    public DataSet(int fps, double cmPerPxl, ArrayList<Point> mousePositions) {
        this.radiusOfEnclosure = radiusOfEnclosure;
        this.fps = fps;
        this.mousePositions = mousePositions;
        this.cmPerPxl = cmPerPxl;

    }

    public double getSpeedAtTime(double time) {
        if (time <= 0) time = 0;
        int frame = (int) (time * fps);
        Point mousePos1 = mousePositions.get(frame);
        Point mousePos2 = mousePositions.get(frame + 1);
        double pixelPerFrame = mousePos1.getDistanceFrom(mousePos2);
        double cmPerFrame = convertPixelsToCm(pixelPerFrame);
        double speed = convertCmPerFrameToCmPerSeconds(cmPerFrame);
        return speed;
    }

    public double getAverageSpeed() {
        double totalDistanceTraveledInCm = getTotalDistanceTraveled();
        double time = mousePositions.size() / ((double) fps);
        averageSpeed = totalDistanceTraveledInCm / time;

        return averageSpeed;
    }

    public double getAverageSpeed(double startTime, double endTime) {
        double i = Math.min(startTime, endTime);
        double j = Math.max(startTime, endTime);
        startTime = i;
        endTime = j;
        double totalDistanceTraveledInCm = getTotalDistanceTraveled(startTime, endTime);
        double time = endTime - startTime;
        averageSpeed = totalDistanceTraveledInCm / time;

        return averageSpeed;
    }


    public Point getPositionAt(double time) {
        int frame = (int) time * fps;
        return mousePositions.get(frame);
    }

    public double getMaxSpeed(double startTime, double endTime) {
        int startframe = (int) startTime * fps;
        int endFrame = (int) endTime * fps;
        if (speeds.size() < endFrame - startframe) {
            for (int frame = 0; frame < mousePositions.size() - 1; frame++) {
                Point mousePos1 = mousePositions.get(frame);
                Point mousePos2 = mousePositions.get(frame + 1);
                double pixelPerFrame = mousePos1.getDistanceFrom(mousePos2);
                double cmPerFrame = convertPixelsToCm(pixelPerFrame);
                double cmPerSec = convertCmPerFrameToCmPerSeconds(cmPerFrame);
                speeds.add(cmPerSec);
            }
        }
        double maxSpeed = 0;
        for (int frame = 0; frame < speeds.size(); frame++) {
            if (frame > startframe && frame < endFrame) {
                double speed = speeds.get(frame);
                if (speed > maxSpeed) maxSpeed = speed;
            }
        }
        return maxSpeed;
    }

    public double getTotalDistanceTraveled() {
        double totalDistanceInPixels = 0;
        for (int i = 0; i < mousePositions.size() - 1; i++) {
            Point mousePos1 = mousePositions.get(i);
            Point mousePos2 = mousePositions.get(i + 1);
            totalDistanceInPixels += mousePos1.getDistanceFrom(mousePos2);
        }
        return totalDistanceInPixels * cmPerPxl;
    }

    public double getTotalDistanceTraveled(double time1, double time2) {
        double totalDistanceInPixels = 0;
        int startFrame = (int) (time1 * fps);
        int endFrame = (int) (time2 * fps);
        for (int frame = startFrame; frame <= endFrame; frame++) {
            Point mousePos1 = mousePositions.get(frame);
            Point mousePos2 = mousePositions.get(frame + 1);
            totalDistanceInPixels += mousePos1.getDistanceFrom(mousePos2);
        }
        return totalDistanceInPixels * cmPerPxl;
    }


    public double getTotalTimeSpentAtSpeedInterval(double speed1, double speed2) {
        int frameCount = 0;
        if (speeds.size() < mousePositions.size() - 1) {
            for (int i = 0; i < mousePositions.size() - 1; i++) {
                Point mousePos1 = mousePositions.get(i);
                Point mousePos2 = mousePositions.get(i + 1);
                double pixelPerFrame = mousePos1.getDistanceFrom(mousePos2);
                double cmPerFrame = convertPixelsToCm(pixelPerFrame);
                double cmPerSec = convertCmPerFrameToCmPerSeconds(cmPerFrame);
                speeds.add(cmPerSec);
            }
        }
        for (int i = 0; i < speeds.size(); i++) {
            double speed = speeds.get(i);
            if (speed >= speed && speed <= speed2) {
                frameCount++;
            }
        }
        double time = frameCount / ((double) fps);
        return time;
    }

    public double getTotalTimeSpentInRegionOfInterest(double startTime, double endTime) {
        int frameCount = 0;
        int startFrame = (int) (startTime * fps);
        int endFrame = (int) (endTime * fps);

        for (int i = 0; i < mousePositions.size(); i++) {
            Point mousePos = mousePositions.get(i);
            for (int roi = 0; roi < regionsOfInterest.size(); roi++) {
                Region ROI = regionsOfInterest.get(roi);
                if (ROI.containsPoint(mousePos.getCol(), mousePos.getRow())) frameCount++;
            }
        }
        double time = frameCount / ((double) fps);
        return time;
    }

    public ArrayList<TimeInterval> getTimeIntervalsSpentInROI() {           //is not working properly
        for (int frame = 1; frame < mousePositions.size(); frame++) {
            Point mousPos = mousePositions.get(frame);
            Point prevMousPos = mousePositions.get(frame - 1);
            boolean isInROI = false;
            boolean previousIsInROI = false;
            for (int i = 0; i < regionsOfInterest.size(); i++) {
                Region ROI = regionsOfInterest.get(i);
                if (ROI.containsPoint(mousPos.getCol(), mousPos.getRow())) isInROI = true;
                if (ROI.containsPoint(prevMousPos.getCol(), prevMousPos.getRow())) previousIsInROI = true;
            }
            if (!previousIsInROI && isInROI) {
                TimeInterval t = new TimeInterval();
                double time = frame / (double) fps;
                t.setStartTime(time);
                timeIntervalsInROI.add(t);
            } else if (previousIsInROI && !isInROI) {
                TimeInterval t = timeIntervalsInROI.get(timeIntervalsInROI.size() - 1);
                double time = (frame - 1) / (double) fps;
                t.setEndTime(time);
            } else if (frame == 1 && previousIsInROI && isInROI) {
                TimeInterval t = new TimeInterval();
                double time = (frame - 1) / (double) fps;
                t.setStartTime(time);
                timeIntervalsInROI.add(t);
            } else if (frame == mousePositions.size() - 1 && isInROI && previousIsInROI) {
                TimeInterval t = timeIntervalsInROI.get(timeIntervalsInROI.size() - 1);
                double time = (frame) / (double) fps;
                t.setEndTime(time);
            }
        }
        return timeIntervalsInROI;
    }

    public double getLongestContinuousDurationOfTimeSpentInRegionOfInterest(double startTime, double endTime) {
        if (timeIntervalsInROI.size() == 0) getTimeIntervalsSpentInROI();
        double maxTimeIntervalDuration = 0;
        for (int i = 0; i < timeIntervalsInROI.size(); i++) {
            TimeInterval t = timeIntervalsInROI.get(i);
            double timeDuration = t.getDurationOfTimeInterval();
            if (timeDuration > maxTimeIntervalDuration) maxTimeIntervalDuration = timeDuration;
        }
        return maxTimeIntervalDuration;
    }

    public double getTotalDistanceTraveledInRegionOfInterest(double startTime, double endTime) {
        double totalDistanceTraveled = 0;
        for (int frames = 0; frames < mousePositions.size() - 1; frames++) {
            double time = frames / (double) fps;
            for (int i = 0; i < timeIntervalsInROI.size(); i++) {
                TimeInterval timeInterval = timeIntervalsInROI.get(i);
                if (timeInterval.isInTimeInterval(time) && timeInterval.isInTimeInterval(time + 1 / (double) fps)) {
                    Point mousePos1 = mousePositions.get(i);
                    Point mousePos2 = mousePositions.get(i + 1);
                    double distanceInPxl = mousePos1.getDistanceFrom(mousePos2);
                    double distanceInCm = distanceInPxl * cmPerPxl;
                    totalDistanceTraveled += distanceInCm;
                }
            }
        }
        return totalDistanceTraveled;
    }

    public double getAvgSpeedInRegionOfInterest(double startTime, double endTime) {
        double timeSpentInRegionOfInterest = 0;
        if (timeIntervalsInROI.size() == 0) getTimeIntervalsSpentInROI();
        for (int i = 0; i < timeIntervalsInROI.size(); i++) {
            TimeInterval t = timeIntervalsInROI.get(i);
            timeSpentInRegionOfInterest += t.getDurationOfTimeInterval();
        }
        if (timeSpentInRegionOfInterest == 0) return -1;
        return getTotalDistanceTraveled() / timeSpentInRegionOfInterest;
    }

    public void deleteFirstRegionOfInterest() {         //bad naming
        regionsOfInterest.remove(0);
    }

    public void addRegionOfInterest(Region r) {
        regionsOfInterest.add(r);
    }

    private double convertPixelsToCm(double pixels) {
        return (pixels) * cmPerPxl;
    }

    private double convertCmPerFrameToCmPerSeconds(double cmPerFrame) {
        return cmPerFrame * fps;
    }


    public void writeDataToFile(String filepath) {
        try (FileWriter f = new FileWriter(filepath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {
            for (int frames = 0; frames < mousePositions.size(); frames++) {
                Point mousePos = mousePositions.get(frames);
                p.println(mousePos.getCol() + "," + mousePos.getRow());
            }

        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filepath);
            error.printStackTrace();
        }
    }

    public ArrayList<Point> readFileAsStringXY(String filepath) {
        mousePositionsFromCSVData = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filepath))) {
            while (sc.hasNext()) {
                String line = sc.nextLine();
                int indexOfComma = line.indexOf(",");
                String x = line.substring(0, indexOfComma);
                String y = line.substring(indexOfComma + 1);
                Point p = new Point(Integer.parseInt(y), Integer.parseInt(x));
                mousePositionsFromCSVData.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mousePositionsFromCSVData;
    }

    public ArrayList<Point> readFileAsStringYX(String filepath) {
        mousePositionsFromCSVData = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filepath))) {
            while (sc.hasNext()) {
                String line = sc.nextLine();
                int indexOfComma = line.indexOf(",");
                String y = line.substring(0, indexOfComma);
                String x = line.substring(indexOfComma + 1);
                Point p = new Point(Integer.parseInt(y), Integer.parseInt(x));
                mousePositionsFromCSVData.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mousePositionsFromCSVData;
    }

}
