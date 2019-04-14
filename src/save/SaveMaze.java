package save;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
     * @param maze  需要转为灰度图的迷宫
     * @param scale 缩放
     * @return
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public static BufferedImage mazeToImage(final Maze maze, final int scale)
            throws NullPointerException, IllegalArgumentException {
        if (maze == null)
            throw new NullPointerException("迷宫不能为null");
        if (scale <= 0)
            throw new IllegalArgumentException("缩放比例必须是正数");

        BufferedImage imageBuffer = new BufferedImage(maze.width * scale, maze.height * scale,
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        for (int y = 0; y < maze.height; y++)
            for (int x = 0; x < maze.width; x++) {
                graphics.setColor(maze.getPassable(x, y) ? Color.WHITE : Color.BLACK);
                graphics.fillRect(x * scale, y * scale, scale, scale);
            }

        return imageBuffer;
    }
    
    public static void saveImage(final RenderedImage image, final String saveFile)
            throws FileNotFoundException, IOException {
        ImageIO.write(image, "jpg", new FileOutputStream(saveFile + ".jpg"));
    }
}
