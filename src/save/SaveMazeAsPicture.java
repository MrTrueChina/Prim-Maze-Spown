package save;

import java.awt.Color;
import java.awt.Graphics2D;
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
public class SaveMazeAsPicture {
    /**
     * 将迷宫以 .jpg 格式存储到项目路径
     * 
     * @param maze     要保存的迷宫
     * @param scale    缩放
     * @param saveFile 保存的图片的文件名
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public static void saveMaze(final Maze maze, final int scale, final String saveFile)
            throws FileNotFoundException, IOException, NullPointerException, IllegalArgumentException {
        if (maze == null)
            throw new NullPointerException("迷宫不能为null");
        if (scale <= 0)
            throw new IllegalArgumentException("缩放比例必须是正数");
        if (saveFile == null)
            throw new NullPointerException("文件名不能为null");
        if (saveFile == "")
            throw new IllegalArgumentException("必须输入文件名");

        BufferedImage imageBuffer = new BufferedImage(maze.width * scale, maze.height * scale,
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        for (int y = 0; y < maze.height; y++)
            for (int x = 0; x < maze.width; x++) {
                graphics.setColor(maze.getPassable(x, y) ? Color.WHITE : Color.BLACK);
                graphics.fillRect(x * scale, y * scale, scale, scale);
            }

        ImageIO.write(imageBuffer, "jpg", new FileOutputStream(saveFile + ".jpg"));
    }
}
