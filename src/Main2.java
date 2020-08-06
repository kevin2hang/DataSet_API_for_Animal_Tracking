import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args) {

        int radius = 207;
        int xcenter = 308;
        int ycenter = 234;
        int fps = 25;
        double cmPerPixel = 1 / 5.316;
        double mouseCenterX, mouseCenterY;

        ArrayList<Point> positions = new ArrayList<>();
        DataSet dataset = new DataSet(fps, cmPerPixel, positions);

        ArrayList<Point> KevinMousePositions = dataset.readFileAsStringXY("Files/ZHANG_KEVIN.csv");
        ArrayList<Point> DobervichMousePositions = dataset.readFileAsStringYX("Files/DOBERVICH_DAVID.csv");

        CircularRegion ROI = new CircularRegion(640, 480);
        ROI.createRegionOfInterest(new Point(233, 308), 100);

        DataSet datasetKevin = new DataSet(fps, cmPerPixel, KevinMousePositions);
        datasetKevin.addRegionOfInterest(ROI);

        DataSet dataSetDobervich = new DataSet(fps, cmPerPixel, DobervichMousePositions);
        dataSetDobervich.addRegionOfInterest(ROI);

        System.out.println("Kevin's Mouse Statistics:");
        System.out.println("Total Distance Traveled: " + datasetKevin.getTotalDistanceTraveled());
        System.out.println("Time spent in region of interest: " + datasetKevin.getTotalTimeSpentInRegionOfInterest(0, 190));
        System.out.println("Max speed: "+datasetKevin.getMaxSpeed(0,190));
        System.out.println("Average speed: " + datasetKevin.getAverageSpeed());
        System.out.println("Time intervals in region of interest: " + datasetKevin.getTimeIntervalsSpentInROI());
        System.out.println("Average speed in region of interest: " + datasetKevin.getAvgSpeedInRegionOfInterest(0, 190));
        System.out.println("Average speed in time interval 30 to 40 seconds: "+datasetKevin.getAverageSpeed(30,40));
        System.out.println("Distance traveled from 50 to 65 seconds: "+datasetKevin.getTotalDistanceTraveled(50,65));

        System.out.println();

        System.out.println("Mr. Dobervich's Mouse Statistics: ");
        System.out.println("Total Distance Traveled: " + dataSetDobervich.getTotalDistanceTraveled());
        System.out.println("Time spent in region of interest: " + dataSetDobervich.getTotalTimeSpentInRegionOfInterest(0, 190));
        System.out.println("Max speed: "+dataSetDobervich.getMaxSpeed(0,190));
        System.out.println("Average speed: " + dataSetDobervich.getAverageSpeed());
        System.out.println("Time intervals in region of interest: " + dataSetDobervich.getTimeIntervalsSpentInROI());
        System.out.println("Average speed in region of interest: " + dataSetDobervich.getAvgSpeedInRegionOfInterest(0, 190));
        System.out.println("Average speed in time interval 30 to 40 seconds: "+dataSetDobervich.getAverageSpeed(30,40));
        System.out.println("Distance traveled from 50 to 65 seconds: "+dataSetDobervich.getTotalDistanceTraveled(50,65));

    }
}
