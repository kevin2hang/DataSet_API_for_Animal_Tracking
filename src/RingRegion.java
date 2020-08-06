public class RingRegion implements Region {
    int[][] grid;

    public RingRegion(int widthOfVideo, int heightOfVideo) {
        grid = new int[heightOfVideo][widthOfVideo];
    }

    public void createRegionOfInterest(Point centerPoint, int innerRadius, int outerRadius) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                double distance = centerPoint.getDistanceFrom(c, r);
                if (distance <= outerRadius && distance >= innerRadius) {
                    grid[r][c]=ROI;
                }
            }
        }
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return grid[y][x]==ROI;
    }
}
