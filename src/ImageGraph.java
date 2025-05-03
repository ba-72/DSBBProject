import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGraph {
    // TODO: 需要实现以下内容
    // 1. 存储像素梯度数据
    // 2. 计算链接成本（根据公式C(x,y) = 1/(1+G(x,y))）
    // 3. 构建8邻接图结构

    int[][] grayPicture=new int[1024][768];//把int[][]的大小改为与导入图片外面加一圈防止数组溢出的数列之后的大小一致
    //TODO:补零（Zero Padding）：外围填充0
    //将图像化为灰度图并且转为矩阵保存在grayPicture
    int[][] Gx=new int[1024][768];//TODO:把int[][]的大小改为与导入图片外面加一圈防止数组溢出的数列之后的大小一致
    int[][] Gy=new int[1024][768];//TODO:把int[][]的大小改为与导入图片外面加一圈防止数组溢出的数列之后的大小一致
    Double[][] Gtotal=new Double[1024][768];
    Double[][] Fg=new Double[1024][768];//标准化的Gtotal
    double Max=Double.MIN_VALUE;
    double[][] cost=new double[1024][768];


    public ImageGraph(BufferedImage image) {
        // 计算Sobel梯度

        int[][] Sx={{-1,0,1},{-2,0,2},{-1,0,1}};
        int[][] Sy={{-1,-2,-1},{0,0,0},{1,2,1}};
        //TODO:这里的时间复杂度是O(n^4)，必须进行优化
        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                for(int k=0;k<=2;k++){
                    for (int m=0;m<=2;m++){
                        Gx[i][j]+=Sx[k][m]*grayPicture[i+k-1][j+m-1];//TODO:防止数组越界
                    }
                }
            }
        }
        //TODO:这也是
        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                for(int k=0;k<=2;k++){
                    for (int m=0;m<=2;m++){
                        Gy[i][j]+=Sy[k][m]*grayPicture[i+k-1][j+m-1];
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
            }
        }

    }

    public double getCost(Point from, Point to) {
        // 返回两个像素之间的链接成本
        for(int i=0;i<grayPicture.length;i++){
            for (int j=0;j<grayPicture[i].length;j++){
                cost[i][j]=1/(1+Gtotal[i][j]);
            }
        }


        return cost[0][0];//TODO:没看明白这里要return什么，需要完善
    }
}