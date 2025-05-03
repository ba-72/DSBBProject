import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import edu.princeton.cs.algs4.*;

public class MainFrame extends JFrame {
    private ImageCanvas imageCanvas;
    boolean isSettingSeed = false;

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
        MouseHandler mouseHandler = new MouseHandler(imageCanvas);
        imageCanvas.addMouseListener(mouseHandler);
        imageCanvas.addMouseMotionListener(mouseHandler);
        mainPanel.add(new JScrollPane(imageCanvas), BorderLayout.CENTER);

        add(mainPanel);

        // TODO: 需要实现事件监听器
        openItem.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent e) {
                                           JFileChooser fileChooser = new JFileChooser();
                                           fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                                                   "Image Files", "jpg", "png", "bmp"));

                                           int result = fileChooser.showOpenDialog(MainFrame.this);
                                           if (result == JFileChooser.APPROVE_OPTION) {
                                               File selectedFile = fileChooser.getSelectedFile();
                                               try {
                                                   BufferedImage image = ImageIO.read(selectedFile);
                                                   imageCanvas.setImage(image);
                                               } catch (IOException ex) {
                                                   JOptionPane.showMessageDialog(MainFrame.this,
                                                           "Error loading image", "Error", JOptionPane.ERROR_MESSAGE);
                                               }
                                           }
                                       }
                                   }
        );  // 实现图片上传
        exitItem.addActionListener(e -> System.exit(0));


        seedButton.addActionListener(e -> {
            isSettingSeed = true; // 进入设置种子点模式
            JOptionPane.showMessageDialog(this, "Click on canvas to set seed point");
        }); // 设置种子点模式

        computeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Path computation triggered");
        }); // 触发路径计算
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}