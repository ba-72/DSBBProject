import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageCanvas extends JPanel {
    private BufferedImage currentImage;
    private Point seedPoint;
    List<Point> currentPath = new ArrayList<>(); // 当前路径
    private List<List<Point>> historicalPaths = new ArrayList<>(); // 历史路径集合
    private Color[] pathColors = {Color.BLUE, Color.CYAN, Color.MAGENTA}; // 不同路径颜色
    private static final double CLOSE_THRESHOLD = 5.0; // 像素距离阈值
    private Point firstSeed;// 记录初始种子点

    // 设置当前路径并触发重绘
    public void setPath(List<Point> path) {
        this.currentPath = new ArrayList<>(path);
        repaint();
    }

    // 添加历史路径段
    public void commitCurrentPath() {
        if (!currentPath.isEmpty()) {
            historicalPaths.add(new ArrayList<>(currentPath));
            currentPath.clear();
            repaint();
        }
    }

    // 清空所有路径
    public void clearAllPaths() {
        historicalPaths.clear();
        currentPath.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制图像
        if (currentImage != null) {
            g2d.drawImage(currentImage, 0, 0, this);
        }

        for (int i = 0; i < historicalPaths.size(); i++) {
            g2d.setColor(pathColors[i % pathColors.length]);
            drawPath(g2d, historicalPaths.get(i));
        }

        g2d.setColor(Color.GREEN);
        drawPath(g2d, currentPath);

        // 绘制种子点
        if (seedPoint != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval(seedPoint.x - 5, seedPoint.y - 5, 10, 10);
        }
    }

    // 辅助方法：绘制路径
    private void drawPath(Graphics2D g2d, List<Point> path) {
        if (path.size() < 2) return;
        Point prev = path.get(0);
        for (Point p : path) {
            g2d.drawLine(prev.x, prev.y, p.x, p.y);
            prev = p;
        }
    }

    public BufferedImage getImage() {
        return currentImage;
    }

    public void setImage(BufferedImage image) {
        this.currentImage = image;
        repaint();
    }

    public void setSeedPoint(Point p) {
        if (this.seedPoint == null) {
            this.firstSeed = p; // 记录第一个种子点
        }
        this.seedPoint = p;
        repaint();
    }

    public Point getSeedPoint() {
        return this.seedPoint;
    }

    public boolean isPathClosed(List<Point> newPath) {
        if (firstSeed == null || newPath.isEmpty()) return false;

        Point lastPoint = newPath.get(newPath.size()-1);
        return lastPoint.distance(firstSeed) < CLOSE_THRESHOLD;
    }
}