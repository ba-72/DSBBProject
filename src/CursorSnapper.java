import java.awt.*;

public class CursorSnapper {
    private static final int SNAP_RADIUS = 5; // 邻域范围（像素）

    // 吸附到梯度最大的邻域点
    public static Point snapToEdge(Point rawPoint, double[][] gradient) {
        int maxX = gradient.length;
        int maxY = gradient[0].length;
        Point bestPoint = rawPoint;
        double maxGradient = 0;

        // 在邻域内搜索最大梯度点
        for (int dx = -SNAP_RADIUS; dx <= SNAP_RADIUS; dx++) {
            for (int dy = -SNAP_RADIUS; dy <= SNAP_RADIUS; dy++) {
                int x = rawPoint.x + dx;
                int y = rawPoint.y + dy;
                if (x >= 0 && x < maxX && y >= 0 && y < maxY) {
                    double g = gradient[x][y];
                    if (g > maxGradient) {
                        maxGradient = g;
                        bestPoint = new Point(x, y);
                    }
                }
            }
        }
        return bestPoint;
    }
}