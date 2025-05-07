import java.awt.*;
import java.util.ArrayList;

public class PathCoolingMonitor {
    private static final double STABILITY_THRESHOLD = 2.0; // 路径稳定阈值（像素）
    private java.util.List<Point> lastPath = new ArrayList<>();

    // 判断路径是否稳定（末端移动距离小于阈值）
    public boolean isPathStable(java.util.List<Point> currentPath) {
        if (lastPath.isEmpty()) return false;

        Point lastEnd = lastPath.get(lastPath.size() - 1);
//        Point currentEnd = currentPath.get(currentPath.size() - 1);
        Point currentEnd = currentPath.get(currentPath.size()-1);
        double distance = lastEnd.distance(currentEnd);

        return distance < STABILITY_THRESHOLD;
    }

    // 更新历史路径
    public void updatePath(java.util.List<Point> path) {
        lastPath = new ArrayList<>(path);
    }
}