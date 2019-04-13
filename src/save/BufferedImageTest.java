/**
 * 用于测试 BufferedImage 的类，与迷宫生成无关
 */

package save;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageTest {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedImageTester tester = new BufferedImageTester();

        tester.defaultTest();

        tester.typeTest("TYPE_3BYTE_BGR 颜色模型的图片默认背景有颜色的，在windows上是黑色", "3BYTE_BGR", BufferedImage.TYPE_3BYTE_BGR,
                "jpg");
        tester.typeTest("使用 .png 后缀名保存 .jpg 图片同样可以保存，但颜色显示和保存为 .jpg 有些许不同", "3BYTE_BGR", BufferedImage.TYPE_3BYTE_BGR,
                "png");
        tester.typeTest("TYPE_4BYTE_ABGR 颜色模型的图片默认背景是透明的", "4BYTE_ABGR", BufferedImage.TYPE_4BYTE_ABGR, "png");
        tester.typeTest("使用 .png 的颜色模型但用 .jpg 后缀名保存会导致图片错误，使用不同的软件打开图片会有不同效果", "4BYTE_ABGR",
                BufferedImage.TYPE_4BYTE_ABGR, "jpg");
    }
}

class BufferedImageTester {
    void defaultTest() throws FileNotFoundException, IOException {
        BufferedImage imageBuffer = new BufferedImage(150, 70, BufferedImage.TYPE_3BYTE_BGR);
        //BufferedImage：图片缓冲区，图片要先在这个缓冲区里处理，处理完毕后再写到硬盘
        //BufferedImage(int width, int height, int imageType) 前两个宽高很明显，第三个是图片的颜色模型，即颜色储存方式
        //BufferedImage.TYPE_BYTE_GRAY：无符号byte灰度图，就是普通的只有黑白灰的灰度图

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();
        // BufferedImage.getGraphics()：获取这个缓冲区的 Graphics2D 对象，但为了兼容性用 Graphics 类型返回
        //、对缓冲区内数据的操作要通过这个 Graphics2D 对象来进行，可以理解为绘制工具、画笔        

        graphics.setColor(Color.CYAN); // 给画笔设置颜色，类似于Unity的 Gizmo.color
        graphics.fillRect(0, 0, 150, 70); // Graphics2D.filllRect：绘制指定的矩形区域，
        //Graphics2D.filllRect(x, y, width, height)：以【左上角】为坐标原点，填充指定的矩形区域

        graphics.setColor(Color.RED);
        graphics.drawRect(0, 0, 150 - 1, 70 - 1);
        //Graphics2D.drawRect(x, y, width, height)：同样以左上角为坐标原点，绘制一个矩形框

        graphics.setFont(new Font("宋体", Font.BOLD, 24));
        //Graphice.setFont(Font font)：给画笔设置字体，用于后续的写文字
        //Font(String name, int style, int size)：
        //name：字体名称，这个字体能否正确使用不知道是取决于电脑字体库还是Java有一个自带字体库
        //style：文字样式，例如普通、加粗、斜体等
        //size：字号

        graphics.drawString("Hello World!", 0, 70);
        //Graphics2D.drawString(String str, int x, int y)：在图片上绘制字符串
        //str：要绘制的字符串
        //x、y：以图片左上角为坐标原点的，以文字左下角为坐标终点的坐标（脑补一下PS文字工具点击会出现下划线，文字在下划线上面）

        ImageIO.write(imageBuffer, "jpg", new FileOutputStream(getJavaFilePath() + "Hello World.jpg"));
        //ImageIO.write(RenderedImage im, String formatName, OutputStream output)
        //RenderedImage im：要输出的图片，必须是实现了 RenderedImage 接口的类的对象，BufferedImage 就是这样的一个类
        //【注意】这里传的是缓冲区，不是画笔
        //String formatName：后缀名，
    }

    void typeTest(final String text, final String fileName, final int imageType, final String formatName)
            throws FileNotFoundException, IOException {
        BufferedImage imageBuffer = new BufferedImage(800, 100, imageType);

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        graphics.setFont(new Font("宋体", Font.BOLD, 18));

        graphics.setColor(Color.RED);
        graphics.drawString(text, 0, 60);

        ImageIO.write(imageBuffer, formatName, new FileOutputStream(getJavaFilePath() + fileName + "." + formatName));
    }

    private String getJavaFilePath() {
        String path = getClass().getResource("").toString(); // getResource("") : 获取这个类的路径，实际上获取的是编译后的 .class 文件的路径
        path = path.replace("file:/", ""); // 获取到的目录最前面有 "file:/" 的前缀，需要去掉才能使用
        path = path.replace("bin", "src"); // 获取到的目录是 bin 文件夹下的 class 文件的目录，需要修改 bin 为 src 才能得到 .java 文件夹下的目录
        return path;
    }
}
