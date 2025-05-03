import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    private final ImageCanvas canvas;

    public MouseHandler(ImageCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO: 设置种子点
        // canvas.setSeedPoint(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO: 实时更新路径（需调用Dijkstra算法）
        // List<Point> path = DijkstraPathFinder.findPath(seedPoint, e.getPoint());
        // canvas.updatePath(path);
    }
}