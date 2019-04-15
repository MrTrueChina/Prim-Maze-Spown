package save;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.Maze;

/**
 * 将迷宫保存为图片的类
 * 
 * @author Administrator
 * @date 2019年4月13日
 *
 */
public class SaveMaze {
    /**
     * 将迷宫转为 byte灰度图并用 BufferedImage 保存返回
     * 
     * @param maze 需要转为灰度图的迷宫
     * @return
     * @throws NullPointerException
     */
    public static BufferedImage mazeToImage(final Maze maze) throws NullPointerException {
        if (maze == null)
            throw new NullPointerException("迷宫不能为null");

        BufferedImage imageBuffer = new BufferedImage(maze.width, maze.height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        for (int y = 0; y < maze.height; y++)
            for (int x = 0; x < maze.width; x++) {
                graphics.setColor(maze.getPassable(x, y) ? Color.WHITE : Color.BLACK);
                graphics.fillRect(x, y, 1, 1);
            }

        return imageBuffer;
    }

    public static void saveImage(final BufferedImage image, final String saveFile, final int scale)
            throws FileNotFoundException, IOException {

        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale); // 获取缩放仿射变换
        //AffineTransfoem.getScaleInstance(double sx, double sy)：按照参数获取一个缩放仿射变换，仿射是个几何学操作，总之这个仿射变换可以按参数缩放图像
        
        AffineTransformOp affineTransformOp = new AffineTransformOp(scaleTransform, null);
        //AffineTransformOp(AffineTransForm xform, RenderingHints hints)：根据仿射变换和渲染提示键创建一个 AffinTransformOp 对象，这个对象是用来执行仿射变换的
        //AffineTranform xform ：需要执行的放射转换
        //RenderingHints hints ：渲染提示键，通过渲染提示键可以对渲染算法做出控制
        
        BufferedImage scaledImage = affineTransformOp.filter(image, null);
        //AffineTransformOp.filter(BufferedImage src, BufferedImage dst)：执行构造时传入的仿射变换，将源 BufferedIamge 变换后输出到目标 BufferedIamge 对象里
        //这个方法同时会返回变换后的 BufferedImage

        ImageIO.write((BufferedImage) scaledImage, "jpg", new FileOutputStream(saveFile + ".jpg"));
    }
}
