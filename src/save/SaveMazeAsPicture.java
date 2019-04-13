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
 * ���Թ�����ΪͼƬ����
 * 
 * @author Administrator
 * @date 2019��4��13��
 *
 */
public class SaveMazeAsPicture {
    /**
     * ���Թ��� .jpg ��ʽ�洢����Ŀ·��
     * 
     * @param maze     Ҫ������Թ�
     * @param scale    ����
     * @param saveFile �����ͼƬ���ļ���
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public static void saveMaze(final Maze maze, final int scale, final String saveFile)
            throws FileNotFoundException, IOException, NullPointerException, IllegalArgumentException {
        if (maze == null)
            throw new NullPointerException("�Թ�����Ϊnull");
        if (scale <= 0)
            throw new IllegalArgumentException("���ű�������������");
        if (saveFile == null)
            throw new NullPointerException("�ļ�������Ϊnull");
        if (saveFile == "")
            throw new IllegalArgumentException("���������ļ���");

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
