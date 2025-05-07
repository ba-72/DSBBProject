import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGraph {
    // TODO: 需要实现以下内容
    // 1. 存储像素梯度数据
    // 2. 计算链接成本（根据公式C(x,y) = 1/(1+G(x,y))）
    // 3. 构建8邻接图结构

    int[][] grayPicture;//把int[][]的大小改为与导入图片外面加一圈防止数组溢出的数列之后的大小一致
    //TODO:补零（Zero Padding）：外围填充0
    //将图像化为灰度图并且转为矩阵保存在grayPicture
    int[][] Gx;//把int[][]的大小改为与导入图片外面加一圈防止数组溢出的数列之后的大小一致
    int[][] Gy;//把int[][]的大小改为与导入图片外面加一圈防止数组溢出的数列之后的大小一致
    double[][] Gtotal;
    double[][] Fg;//标准化的Gtotal
    double Max=Double.MIN_VALUE;
    int[][] rgb;
    int[][] gray;
    double[][] cost;

    public ImageGraph(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        grayPicture = new int[width][height];
        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                int rgb = image.getRGB(i,j);
                grayPicture[i][j] = (rgb >> 16) & 0xFF;
            }
        }

//        grayPicture = new int[width + 2][height + 2]; // 外围默认填充0
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                int rgb = image.getRGB(i, j);
//                grayPicture[i + 1][j + 1] = (rgb >> 16) & 0xFF; // 原图放在中心，外围是0
//            }
//        }

        Gx = new int[width][height];
        Gy = new int[width][height];
//        Gx = new int[width + 2][height + 2];
//        Gy = new int[width + 2][height + 2];

        Gtotal = new double[width][height];
        Fg = new double[width][height];
        cost = new double[width][height];

//        Gtotal = new double[width+2][height+2];
//        Fg = new double[width+2][height+2];
//        cost = new double[width+2][height+2];

        computeSobelGradient(image); // 计算梯度
    }


    public void computeSobelGradient(BufferedImage image){
        // 计算Sobel梯度

        int[][] Sx={{-1,0,1},{-2,0,2},{-1,0,1}};
        int[][] Sy={{-1,-2,-1},{0,0,0},{1,2,1}};
        //TODO:这里的时间复杂度是O(n^4)，必须进行优化
        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                for(int k=0;k<=2;k++){
                    for (int m=0;m<=2;m++){
                        if(i+k-1<0 || i+k-1>=grayPicture.length || j+m-1<0 || j+m-1>=grayPicture[0].length){
                            Gx[i][j]+=0;
                        }else {
                            Gx[i][j]+=Sx[k][m]*grayPicture[i+k-1][j+m-1];//TODO:防止数组越界
                        }
                    }
                }
            }
        }
        //TODO:这也是
        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                for(int k=0;k<=2;k++){
                    for (int m=0;m<=2;m++){
//                        Gy[i][j]+=Sy[k][m]*grayPicture[i+k-1][j+m-1];
                        if(i+k-1<0 || i+k-1>=grayPicture.length || j+m-1<0 || j+m-1>=grayPicture[0].length){
                            Gy[i][j]+=0;
                        }else {
                            Gy[i][j]+=Sy[k][m]*grayPicture[i+k-1][j+m-1];//TODO:防止数组越界
                        }
                    }
                }
            }
        }

        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                Gtotal[i][j]=Math.sqrt(Math.pow((double) Gx[i][j],2)+Math.pow((double) Gy[i][j],2));
            }
        }



        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                if(Gtotal[i][j]>=Max){
                    Max=Gtotal[i][j];
                }
            }
        }

        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                Fg[i][j]=(Max-Gtotal[i][j])/Max;
                cost[i][j] = 1.0 / (1.0 + Gtotal[i][j]);
            }
        }

    }

    public double getCost(Point from, Point to) {
        // 返回两个像素之间的链接成本
//        for(int i=0;i<grayPicture.length;i++){
//            for (int j=0;j<grayPicture[i].length;j++){
//                cost[i][j]=1/(1+Gtotal[i][j]);
//            }
//        }
//        return cost[0][0];//TODO:没看明白这里要return什么，需要完善

        if (to.x < 0 || to.x >= grayPicture.length || to.y < 0 || to.y >= grayPicture[0].length) {
            return Double.POSITIVE_INFINITY; // 返回无限大成本表示无效连接
        }

        // 检查是否為 8 邻接
        int dx = Math.abs(to.x - from.x);
        int dy = Math.abs(to.y - from.y);
        if (dx > 1 || dy > 1) {
            throw new IllegalArgumentException("非 8 邻接像素");
        }
        // 获取目标点的梯度强度 G
//        System.out.println(to.x);
//        System.out.println(to.y);
        double G = Gtotal[to.x][to.y];
        // 计算成本（梯度越高，成本越低）
        double cost = 1.0 / (1.0 + G);
        // 斜向移动额外处理（如乘以 sqrt(2)）
        if (dx != 0 && dy != 0) {
            cost *= Math.sqrt(2); // 可选：斜向移动成本更高
        }
        return cost;
    }
}
