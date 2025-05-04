import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.SwingUtilities;

public class MouseHandler extends MouseAdapter {
    private final ImageCanvas canvas;
    private final MainFrame mainFrame;
    private ImageGraph imageGraph; // 新增：用于路径计算

    public MouseHandler(ImageCanvas canvas, MainFrame mainFrame) {
        this.canvas = canvas;
        this.mainFrame = mainFrame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mainFrame.isSettingSeed) {
            // 设置种子点
            Point seed = e.getPoint();
            canvas.setSeedPoint(seed);
            mainFrame.isSettingSeed = false;

            // 初始化图像梯度图（首次点击时加载）
            if (imageGraph == null) {
                imageGraph = new ImageGraph(canvas.getImage());
            }
            mainFrame.setImageGraph(imageGraph); // 将graph传递给MainFrame
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // 实时路径更新（仅在已设置种子点时触发）
        if (imageGraph != null && canvas.getSeedPoint() != null) {
            Point currentPoint = e.getPoint();

            // 限制计算频率（每50ms触发一次）
            if (System.currentTimeMillis() - mainFrame.lastUpdateTime > 50) {
                calculateAndDrawPath(currentPoint);
                mainFrame.lastUpdateTime = System.currentTimeMillis();
            }
        }
    }

    private void calculateAndDrawPath(Point end) {
        // 异步计算避免界面卡顿
        new Thread(() -> {
            List<Point> path = Dijkstra.findPath(imageGraph, canvas.getSeedPoint(), end);

            // 在EDT中更新UI
            SwingUtilities.invokeLater(() -> {
                canvas.setPath(path);
                canvas.repaint();
            });
        }).start();
    }
}