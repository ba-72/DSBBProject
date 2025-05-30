import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collections;

import edu.princeton.cs.algs4.*;

public class MainFrame extends JFrame {
    public static boolean Close=false;
    private ImageCanvas imageCanvas;
    boolean isSettingSeed = false;
    private Point seedPoint; // 新增：存储当前种子点
    protected ImageGraph imageGraph; // 新增：图像梯度数据
    protected long lastUpdateTime = 0; // 用于限流
    private double[][] gradient;
    private PathCoolingMonitor coolingMonitor = new PathCoolingMonitor();//添加冷却监控器


    public void setImageGraph(ImageGraph graph) {
        this.imageGraph = graph;
    }

    public MainFrame() {
        setTitle("Intelligent Scissors");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化组件
        initComponents();
//        imageCanvas.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (isSettingSeed) {
//                    seedPoint = e.getPoint();
//                    imageCanvas.setSeedPoint(seedPoint);
//                    isSettingSeed = false;
//
//                    if (imageGraph == null) {
//                        imageGraph = new ImageGraph(imageCanvas.getImage());
//                        gradient = imageGraph.getGradient(); // 加载梯度数据
//                    }
//                }
//            }
//        });
    }

    private void initComponents() {

        // 1. 菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open Image");
        JMenuItem saveItem = new JMenuItem("Save Result");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        imageCanvas = new ImageCanvas();

        // 2. 主布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 左侧控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
//        JButton seedButton = new JButton("Set Seed Point");
//        JButton computeButton = new JButton("Compute Path");
//        controlPanel.add(seedButton);
//        controlPanel.add(computeButton);
//        mainPanel.add(controlPanel, BorderLayout.WEST);

        // 图像显示区域
        imageCanvas = new ImageCanvas();
        MouseHandler mouseHandler = new MouseHandler(imageCanvas,this);
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

        saveItem.addActionListener(e -> {
            saveImage();
        });

        exitItem.addActionListener(e -> System.exit(0));


//        seedButton.addActionListener(e -> {
//            System.out.println(1);
//            isSettingSeed = true; // 进入设置种子点模式
//            JOptionPane.showMessageDialog(this, "Click on canvas to set seed point");
//        }); // 设置种子点模式

//        computeButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Path computation triggered");
//        }); // 触发路径计算

//        imageCanvas.addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                if (seedPoint != null) {
//                    // 实时计算路径（在鼠标移动时触发）
//                    java.util.List<Point> path = calculatePath(seedPoint, e.getPoint());
//                    imageCanvas.setPath(path);
//                }
//            }
//        });

//        System.out.println(1);

        imageCanvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

//                System.out.println(2);
                if (seedPoint != null/* && imageGraph != null*/) {
//                    System.out.println(3);
                    if(gradient==null){
                        gradient=imageGraph.getGtotal();
                    }

                    // 实时计算路径（在鼠标移动时触发）
                    Point snappedPoint = CursorSnapper.snapToEdge(e.getPoint(), gradient);
                    // 计算路径并更新显示
                    java.util.List<Point> path = calculatePath(seedPoint, snappedPoint);
                    imageCanvas.setPath(path);
                }
//                if(seedPoint==null){
//                    System.out.println(3);
//                }
            }
        });
    }

//    private java.util.List<Point> calculatePath(Point start, Point end) {
//        if (imageGraph == null) return Collections.emptyList();
//        return Dijkstra.findPath(imageGraph, start, end); // 调用算法
//    }

    private java.util.List<Point> calculatePath(Point start, Point end) {
        if (imageGraph == null) return Collections.emptyList();

//        System.out.println(0);
        java.util.List<Point> path = Dijkstra.findPath(imageGraph, start, end);

        // 路径冷却：当路径稳定时自动生成新种子点
        if (coolingMonitor.isPathStable(path)) {
//            System.out.println(1);
            Point newSeed = path.get(path.size() - 1);
            seedPoint = newSeed;
            imageCanvas.setSeedPoint(newSeed);
            coolingMonitor.updatePath(Collections.emptyList()); // 重置状态
        } else {
            coolingMonitor.updatePath(path);
        }

        return path;
    }


//    private class MouseHandler extends MouseAdapter {
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            if (isSettingSeed) {
//                seedPoint = e.getPoint();
//                imageCanvas.setSeedPoint(seedPoint);
//                isSettingSeed = false;
//
//                // 初始化图像梯度图（首次点击时加载）
//                if (imageGraph == null) {
//                    imageGraph = new ImageGraph(imageCanvas.getImage());
//                }
//            }
//        }
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // MainFrame.java 中的打开图像方法
    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);
                System.out.println(3);
                imageCanvas.setImage(image); // 确保此处正确设置图像
                System.out.println(4);
                imageCanvas.repaint();
                System.out.println(5);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "加载图像失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage() {
        if (imageCanvas.getImage() == null) {
            JOptionPane.showMessageDialog(this, "No image loaded", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "PNG Images", "png"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

            try {
                BufferedImage processedImage = imageCanvas.createMaskedImage();
                ImageIO.write(processedImage, "png", file);
                JOptionPane.showMessageDialog(this, "Image saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
