import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ImageCanvas imageCanvas;

    public MainFrame() {
        setTitle("Intelligent Scissors");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化组件
        initComponents();
    }

    private void initComponents() {
        // 1. 菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open Image");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // 2. 主布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 左侧控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JButton seedButton = new JButton("Set Seed Point");
        JButton computeButton = new JButton("Compute Path");
        controlPanel.add(seedButton);
        controlPanel.add(computeButton);
        mainPanel.add(controlPanel, BorderLayout.WEST);

        // 图像显示区域
        imageCanvas = new ImageCanvas();
        mainPanel.add(new JScrollPane(imageCanvas), BorderLayout.CENTER);

        add(mainPanel);

        // TODO: 需要实现事件监听器
        // openItem.addActionListener(...);  // 实现图片上传
        // seedButton.addActionListener(...); // 设置种子点模式
        // computeButton.addActionListener(...); // 触发路径计算
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}