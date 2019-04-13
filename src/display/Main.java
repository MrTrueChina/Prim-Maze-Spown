package display;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EventListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import maze.Maze;
import maze.MazeSpowner;
import save.SaveMazeAsPicture;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //主窗口
        JFrame mainWindow = new JFrame("迷宫生成");
        mainWindow.setSize(600, 800);
        mainWindow.setLayout(null);
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //迷宫图片
        ImageIcon icon = new ImageIcon();
        icon.setImage(new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(25, 25, 550, 550);
        mainWindow.add(imageLabel);

        //生成按钮
        JButton spownButton = new JButton("生成迷宫");
        spownButton.setSize(200, 50);
        spownButton.setLocation(200, 600);
        spownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        mainWindow.add(spownButton);

        mainWindow.setVisible(true);

        long startSpownTime = System.currentTimeMillis();
        Maze maze = MazeSpowner.spown(50, 50);
        System.out.println("生成总时间 = " + (System.currentTimeMillis() - startSpownTime) + "毫秒");

        SaveMazeAsPicture.saveMaze(maze, 3, "迷宫");

        System.out.println("生成完成");
    }
}

class MainWindow {

}
