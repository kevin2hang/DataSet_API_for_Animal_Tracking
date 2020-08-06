public class CircularRegion implements Region {
    int[][] grid;

    public CircularRegion(int widthOfVideo, int heightOfVideo) {
        grid = new int[heightOfVideo][widthOfVideo];
    }


    public void createRegionOfInterest(Point centerPoint, int radius) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (centerPoint.getDistanceFrom(c, r) <= radius) {
                    grid[r][c] = ROI;
                }
            }
        }
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return grid[y][x] == ROI;
    }
}

