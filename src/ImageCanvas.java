import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageCanvas extends JPanel {
    private BufferedImage currentImage;
    private Point seedPoint;
    private java.util.List<Point> pathPoints;
    private java.util.List<Point> path = new ArrayList<>();
    private BufferedImage image;

    public BufferedImage getImage() {
//        return this.image;
        return this.currentImage;
    }

    // 新增：设置路径并重绘
    public void setPath(java.util.List<Point> path) {
        this.path = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制图像
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, this);
            System.out.println(2);
        }

        // 绘制种子点（红色圆圈）
        if (seedPoint != null) {
            g.setColor(Color.RED);
            g.fillOval(seedPoint.x - 5, seedPoint.y - 5, 10, 10);
        }

        // 新增：绘制路径
        if (!path.isEmpty()) {
            g.setColor(Color.BLUE);
            Point prev = path.get(0);
            for (Point p : path) {
                g.drawLine(prev.x, prev.y, p.x, p.y);
                prev = p;
            }
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

    public Point getSeedPoint() {
        return this.seedPoint;
    }
}