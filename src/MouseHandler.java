import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javax.swing.*;

public class MouseHandler extends MouseAdapter {
    private final ImageCanvas canvas;
    private final MainFrame mainFrame;
    private ImageGraph imageGraph;
    private Deque<List<Point>> pathHistory = new ArrayDeque<>(3);
    private List<List<Point>> historicalPaths = new ArrayList<>(); // 历史路径集合
    private Point firstSeed; // 初始种子点

    public MouseHandler(ImageCanvas canvas, MainFrame mainFrame) {
        this.canvas = canvas;
        this.mainFrame = mainFrame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickedPoint = e.getPoint(); // 获取鼠标点击坐标

        if (canvas.getSeedPoint() == null) {
            // 首次点击：设置初始种子点
            canvas.setSeedPoint(clickedPoint);
            firstSeed = clickedPoint;
        } else {
            // 后续点击：提交当前路径，并设置新种子点为鼠标点击位置
            if (!canvas.currentPath.isEmpty()) {
                canvas.commitCurrentPath(); // 提交当前路径到历史
            }
            canvas.setSeedPoint(clickedPoint); // 关键修改：新种子点为点击位置
        }

        // 初始化或刷新梯度图
        if (imageGraph == null) {
            imageGraph = new ImageGraph(canvas.getImage());
        }
        mainFrame.setImageGraph(imageGraph);
    }

    private static final int SNAP_RADIUS = 5;

    @Override
    public void mouseMoved(MouseEvent e) {
        if (imageGraph != null && canvas.getSeedPoint() != null) {
            // 原始鼠标位置
            Point rawPoint = e.getPoint();
            // 执行光标吸附
            Point snappedPoint = snapToEdge(rawPoint);
            // 限制计算频率
            if (System.currentTimeMillis() - mainFrame.lastUpdateTime > 50 && MainFrame.Close==false) {
                calculateAndDrawPath(snappedPoint); // 使用吸附后的坐标
                mainFrame.lastUpdateTime = System.currentTimeMillis();
            }
        }
    }

    private Point snapToEdge(Point rawPoint) {
        int maxX = imageGraph.Gtotal.length;
        int maxY = imageGraph.Gtotal[0].length;
        double maxGradient = -1;
        Point bestPoint = rawPoint;

        // 遍历以rawPoint为中心的邻域
        for (int dx = -SNAP_RADIUS; dx <= SNAP_RADIUS; dx++) {
            for (int dy = -SNAP_RADIUS; dy <= SNAP_RADIUS; dy++) {
                int x = rawPoint.x + dx;
                int y = rawPoint.y + dy;
                // 边界检查
                if (x >= 0 && x < maxX && y >= 0 && y < maxY) {
                    double gradient = imageGraph.Gtotal[x][y];
                    if (gradient > maxGradient) {
                        maxGradient = gradient;
                        bestPoint = new Point(x, y);
                    }
                }
            }
        }
        return bestPoint;
    }

    private void updatePathHistory(List<Point> newPath) {
        if (pathHistory.size() >= 3) {
            pathHistory.pollFirst();
        }
        pathHistory.offerLast(new ArrayList<>(newPath));
    }

    private boolean isPathStable() {
        if (pathHistory.size() < 3) return false;

        List<Point> path1 = pathHistory.pollFirst();
        List<Point> path2 = pathHistory.peekFirst();
        double diff = calculatePathDifference(path1, path2);
        return diff < 5.0;
    }

    private double calculatePathDifference(List<Point> pathA, List<Point> pathB) {
        int minLength = Math.min(pathA.size(), pathB.size());
        double totalDiff = 0;
        for (int i = 0; i < minLength; i++) {
            Point p1 = pathA.get(i);
            Point p2 = pathB.get(i);
            totalDiff += p1.distance(p2);
        }
        return totalDiff / minLength; // 平均差异
    }

    private void calculateAndDrawPath(Point end) {
        new Thread(() -> {
            // 确定路径起点：若存在历史路径，则从最后一个路径的末端开始
            Point startPoint = canvas.getSeedPoint();
            if (!historicalPaths.isEmpty()) {
                List<Point> lastPath = historicalPaths.get(historicalPaths.size() - 1);
                startPoint = lastPath.get(lastPath.size() - 1);
            }

            // 调用Dijkstra算法计算路径
            List<Point> path = Dijkstra.findPath(imageGraph, startPoint, end);

            // 检测闭合条件
            if (isPathClosed(path)) {

                historicalPaths.add(path); // 保存闭合路径
                canvas.commitCurrentPath();
                JOptionPane.showMessageDialog(canvas, "路径已闭合！");
                MainFrame.Close=true;
            } else {
                // 更新当前路径
                SwingUtilities.invokeLater(() -> {
                    canvas.setPath(path);
                    canvas.repaint();
                });
            }
        }).start();
    }

    private boolean isPathClosed(List<Point> path) {
        if (path.isEmpty() || firstSeed == null || path.size() < 10) return false; // 路径长度需大于10
        Point lastPoint = path.get(path.size() - 1);
        return lastPoint.distance(firstSeed) < 5.0;
    }   // 阈值5像素
}