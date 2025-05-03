import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageCanvas extends JPanel {
    private BufferedImage currentImage;
    private Point seedPoint;
    private java.util.List<Point> pathPoints;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制图像
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, this);
        }

        // 绘制种子点（红色圆圈）
        if (seedPoint != null) {
            g.setColor(Color.RED);
            g.fillOval(seedPoint.x - 5, seedPoint.y - 5, 10, 10);
        }

        // TODO: 绘制实时路径（绿色线条）
        // if (pathPoints != null) {
        //   g.setColor(Color.GREEN);
        //   for (Point p : pathPoints) { ... }
        // }
    }

    // TODO: 需要实现以下方法
    public void setImage(BufferedImage image) {
        this.currentImage = image;
        repaint();
    }

    public void setSeedPoint(Point p) {
        this.seedPoint = p;
        repaint();
    }

    public void updatePath(java.util.List<Point> path) {
        this.pathPoints = path;
        repaint();
    }
}