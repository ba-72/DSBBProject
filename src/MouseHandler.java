import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MouseHandler extends MouseAdapter {
    private final ImageCanvas canvas;
    private final MainFrame mainFrame;

    public MouseHandler(ImageCanvas canvas) {
        this.canvas = canvas;
        this.mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(canvas);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mainFrame.isSettingSeed) {
            // 设置种子点
            canvas.setSeedPoint(e.getPoint());
            mainFrame.isSettingSeed = false; // 退出设置模式
            JOptionPane.showMessageDialog(canvas, "Seed set at: " + e.getPoint());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // 实时路径更新（示例空实现，需连接Dijkstra算法）
        // List<Point> path = DijkstraPathFinder.findPath(seed, e.getPoint());
        // canvas.updatePath(path);
    }
}