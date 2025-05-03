import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGraph {
    // TODO: 需要实现以下内容
    // 1. 存储像素梯度数据
    // 2. 计算链接成本（根据公式C(x,y) = 1/(1+G(x,y))）
    // 3. 构建8邻接图结构

    public ImageGraph(BufferedImage image) {
        // 计算Sobel梯度
    }

    public double getCost(Point from, Point to) {
        // 返回两个像素之间的链接成本
        return 0.0;
    }
}