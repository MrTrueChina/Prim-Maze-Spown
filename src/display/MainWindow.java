package display;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import maze.MazeSpowner;
import save.SaveMaze;

public class MainWindow {
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private static final int DEFAULT_SCALE = 3;

    private BufferedImage _mazeImage;
    private JLabel _imageLabel;
    private JButton _spownButton;
    private JButton _saveButton;
    private JTextField _widthText;
    private JTextField _heightText;
    private JTextField _scaleText;

    private int _mazeWidth;
    private int _mazeHeight;
    private int _mazeImageScale;

    public MainWindow() throws FileNotFoundException, IOException {
        setup();
    }

    private void setup() throws FileNotFoundException, IOException {
        setupMazeImage();
        setupUI();
    }

    private void setupMazeImage() {
        _mazeWidth = DEFAULT_WIDTH;
        _mazeHeight = DEFAULT_HEIGHT;
        _mazeImageScale = DEFAULT_SCALE;

        _mazeImage = getMazeImage();
    }

    private void setupUI() {
        JFrame mainWindow = getMainWindow();

        mainWindow.add(setupedImageLabel());
        mainWindow.add(setupSpownButton());
        mainWindow.add(setupSaveButton());
        mainWindow.add(setupWidthText());
        mainWindow.add(setupHeightText());
        mainWindow.add(setupScaleText());

        mainWindow.setVisible(true);
    }

    private JFrame getMainWindow() {
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

    private JLabel setupedImageLabel() {
        _imageLabel = new JLabel("", JLabel.CENTER);
        _imageLabel.setBounds(25, 25, 550, 550);
        setMazeImageToImageLabel();
        return _imageLabel;
    }

    private JTextField setupWidthText() {
        _widthText = new JTextField(DEFAULT_WIDTH);
        _widthText.setBounds(25, 600, 150, 30);
        _widthText.setText(_mazeWidth + "");

        _widthText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (_widthText.getText().matches("[1-9][0-9]*"))
                    _mazeWidth = Integer.parseInt(_widthText.getText());
                else
                    _widthText.setText(_mazeWidth + "");
            }
        });

        return _widthText;
    }

    private JTextField setupHeightText() {
        _heightText = new JTextField(DEFAULT_WIDTH);
        _heightText.setBounds(225, 600, 150, 30);
        _heightText.setText(_mazeHeight + "");

        _heightText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (_heightText.getText().matches("[1-9][0-9]*"))
                    _mazeHeight = Integer.parseInt(_heightText.getText());
                else
                    _heightText.setText(_mazeHeight + "");
            }
        });

        return _heightText;
    }

    private JTextField setupScaleText() {
        _scaleText = new JTextField(DEFAULT_SCALE);
        _scaleText.setBounds(425, 600, 150, 30);
        _scaleText.setText(_mazeImageScale + "");

        _scaleText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (_scaleText.getText().matches("[1-9][0-9]*"))
                    _mazeImageScale = Integer.parseInt(_scaleText.getText());
                else
                    _scaleText.setText(_mazeImageScale + "");
            }
        });

        return _scaleText;
    }

    private JButton setupSpownButton() {
        _spownButton = new JButton("生成迷宫");
        _spownButton.setBounds(50, 650, 200, 50);
        _spownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _mazeImage = getMazeImage();
                setMazeImageToImageLabel();
            }
        });

        return _spownButton;
    }

    private JButton setupSaveButton() {
        _saveButton = new JButton("保存地图");
        _saveButton.setBounds(320, 650, 200, 50);
        _saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SaveMaze.saveImage(_mazeImage, "迷宫", _mazeImageScale);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        return _saveButton;
    }

    private BufferedImage getMazeImage() {
        //        System.out.println("_mazeWidht = " + _mazeWidth);
        //        System.out.println("_mazeHeight = " + _mazeHeight);
        //        System.out.println("_mazeImageScale = " + _mazeImageScale);

        return SaveMaze.mazeToImage(MazeSpowner.spown(_mazeWidth, _mazeHeight));
    }

    private void setMazeImageToImageLabel() {
        Image scaledMazeImage;
        if (_mazeImage.getWidth() > _mazeImage.getHeight())
            scaledMazeImage = _mazeImage.getScaledInstance(550, -1, Image.SCALE_FAST);
        else
            scaledMazeImage = _mazeImage.getScaledInstance(-1, 550, Image.SCALE_FAST);

        _imageLabel.setIcon(new ImageIcon(scaledMazeImage));
    }
}
