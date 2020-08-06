public class SquareRegion implements Region{
    int[][] grid;
    public SquareRegion(int widthOfVideo, int heightOfVideo) {
        grid = new int[heightOfVideo][widthOfVideo];
    }

    public void createRegionOfInterest(int x1, int y1, int x2, int y2) {
        for (int r = y1; r <= y2; r++) {
            for (int c = x1; c <= x2; c++) {
                grid[r][c] = ROI;
            }
        }
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return grid[y][x]==ROI;
    }
}
