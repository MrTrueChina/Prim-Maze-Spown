package display;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import maze.MazeSpowner;
import save.SaveMaze;

public class Main {
    static BufferedImage _mazeImage = SaveMaze.mazeToImage(MazeSpowner.spown(50, 50), 5);
    static JLabel _imageLabel;

    public static void main(String[] args) throws FileNotFoundException, IOException {

        JFrame mainWindow = getMainWindow();

        //、迷宫图片
        _imageLabel = new JLabel();
        _imageLabel.setBounds(25, 25, 550, 550);
        _imageLabel.setIcon(new ImageIcon(_mazeImage.getScaledInstance(550, -1, Image.SCALE_FAST)));
        mainWindow.add(_imageLabel);

        //、生成按钮
        JButton spownButton = new JButton("生成迷宫");
        spownButton.setBounds(50, 600, 200, 50);
        spownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _mazeImage = SaveMaze.mazeToImage(MazeSpowner.spown(50, 50), 5);
                _imageLabel.setIcon(new ImageIcon(_mazeImage.getScaledInstance(550, -1, Image.SCALE_FAST)));
            }
        });
        mainWindow.add(spownButton);

        //保存按钮
        JButton saveButton = new JButton("保存地图");
        saveButton.setBounds(320, 600, 200, 50);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SaveMaze.saveImage(_mazeImage, "迷宫");
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        mainWindow.add(saveButton);

        mainWindow.setVisible(true);
    }

    private static JFrame getMainWindow() {
        JFrame mainWindow = new JFrame("迷宫生成器");

        mainWindow.setSize(600, 800);
        mainWindow.setLayout(null);
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return mainWindow;
    }
}
