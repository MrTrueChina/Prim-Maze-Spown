/**
 * ���ڲ��� BufferedImage ���࣬���Թ������޹�
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

        tester.typeTest("TYPE_3BYTE_BGR ��ɫģ�͵�ͼƬĬ�ϱ�������ɫ�ģ���windows���Ǻ�ɫ", "3BYTE_BGR", BufferedImage.TYPE_3BYTE_BGR,
                "jpg");
        tester.typeTest("ʹ�� .png ��׺������ .jpg ͼƬͬ�����Ա��棬����ɫ��ʾ�ͱ���Ϊ .jpg ��Щ��ͬ", "3BYTE_BGR", BufferedImage.TYPE_3BYTE_BGR,
                "png");
        tester.typeTest("TYPE_4BYTE_ABGR ��ɫģ�͵�ͼƬĬ�ϱ�����͸����", "4BYTE_ABGR", BufferedImage.TYPE_4BYTE_ABGR, "png");
        tester.typeTest("ʹ�� .png ����ɫģ�͵��� .jpg ��׺������ᵼ��ͼƬ����ʹ�ò�ͬ�������ͼƬ���в�ͬЧ��", "4BYTE_ABGR",
                BufferedImage.TYPE_4BYTE_ABGR, "jpg");
    }
}

class BufferedImageTester {
    void defaultTest() throws FileNotFoundException, IOException {
        BufferedImage imageBuffer = new BufferedImage(150, 70, BufferedImage.TYPE_3BYTE_BGR);
        //BufferedImage��ͼƬ��������ͼƬҪ��������������ﴦ��������Ϻ���д��Ӳ��
        //BufferedImage(int width, int height, int imageType) ǰ������ߺ����ԣ���������ͼƬ����ɫģ�ͣ�����ɫ���淽ʽ
        //BufferedImage.TYPE_BYTE_GRAY���޷���byte�Ҷ�ͼ��������ͨ��ֻ�кڰ׻ҵĻҶ�ͼ

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();
        // BufferedImage.getGraphics()����ȡ����������� Graphics2D ���󣬵�Ϊ�˼������� Graphics ���ͷ���
        //���Ի����������ݵĲ���Ҫͨ����� Graphics2D ���������У��������Ϊ���ƹ��ߡ�����        

        graphics.setColor(Color.CYAN); // ������������ɫ��������Unity�� Gizmo.color
        graphics.fillRect(0, 0, 150, 70); // Graphics2D.filllRect������ָ���ľ�������
        //Graphics2D.filllRect(x, y, width, height)���ԡ����Ͻǡ�Ϊ����ԭ�㣬���ָ���ľ�������

        graphics.setColor(Color.RED);
        graphics.drawRect(0, 0, 150 - 1, 70 - 1);
        //Graphics2D.drawRect(x, y, width, height)��ͬ�������Ͻ�Ϊ����ԭ�㣬����һ�����ο�

        graphics.setFont(new Font("����", Font.BOLD, 24));
        //Graphice.setFont(Font font)���������������壬���ں�����д����
        //Font(String name, int style, int size)��
        //name���������ƣ���������ܷ���ȷʹ�ò�֪����ȡ���ڵ�������⻹��Java��һ���Դ������
        //style��������ʽ��������ͨ���Ӵ֡�б���
        //size���ֺ�

        graphics.drawString("Hello World!", 0, 70);
        //Graphics2D.drawString(String str, int x, int y)����ͼƬ�ϻ����ַ���
        //str��Ҫ���Ƶ��ַ���
        //x��y����ͼƬ���Ͻ�Ϊ����ԭ��ģ����������½�Ϊ�����յ�����꣨�Բ�һ��PS���ֹ��ߵ��������»��ߣ��������»������棩

        ImageIO.write(imageBuffer, "jpg", new FileOutputStream(getJavaFilePath() + "Hello World.jpg"));
        //ImageIO.write(RenderedImage im, String formatName, OutputStream output)
        //RenderedImage im��Ҫ�����ͼƬ��������ʵ���� RenderedImage �ӿڵ���Ķ���BufferedImage ����������һ����
        //��ע�⡿���ﴫ���ǻ����������ǻ���
        //String formatName����׺����
    }

    void typeTest(final String text, final String fileName, final int imageType, final String formatName)
            throws FileNotFoundException, IOException {
        BufferedImage imageBuffer = new BufferedImage(800, 100, imageType);

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        graphics.setFont(new Font("����", Font.BOLD, 18));

        graphics.setColor(Color.RED);
        graphics.drawString(text, 0, 60);

        ImageIO.write(imageBuffer, formatName, new FileOutputStream(getJavaFilePath() + fileName + "." + formatName));
    }

    private String getJavaFilePath() {
        String path = getClass().getResource("").toString(); // getResource("") : ��ȡ������·����ʵ���ϻ�ȡ���Ǳ����� .class �ļ���·��
        path = path.replace("file:/", ""); // ��ȡ����Ŀ¼��ǰ���� "file:/" ��ǰ׺����Ҫȥ������ʹ��
        path = path.replace("bin", "src"); // ��ȡ����Ŀ¼�� bin �ļ����µ� class �ļ���Ŀ¼����Ҫ�޸� bin Ϊ src ���ܵõ� .java �ļ����µ�Ŀ¼
        return path;
    }
}
