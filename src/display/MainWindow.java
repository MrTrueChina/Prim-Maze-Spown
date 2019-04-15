package display;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import maze.Maze;
import maze.MazeSpowner;
import save.SaveMaze;

public class MainWindow {
    private Maze _maze;
    private JLabel _imageLabel;
    private JButton _spownButton;
    private JButton _saveButton;
    private JTextField _widthText;
    private JTextField _heightText;
    private JTextField _scaleText;
    private Thread _displayThread;
    private Thread _spownThread;

    private int _mazeWidth = 20;
    private int _mazeHeight = 20;
    private int _mazeImageScale = 5;

    public MainWindow() throws FileNotFoundException, IOException {
        setup();
    }

    private void setup() throws FileNotFoundException, IOException {
        JFrame mainWindow = getMainWindow();

        mainWindow.add(setupedImageLabel());
        mainWindow.add(setupSpownButton());
        mainWindow.add(setupSaveButton());
        mainWindow.add(setUpWidthLabel());
        mainWindow.add(setupWidthText());
        mainWindow.add(setUpHeightLabel());
        mainWindow.add(setupHeightText());
        mainWindow.add(setUpScaleLabel());
        mainWindow.add(setupScaleText());

        mainWindow.setVisible(true);
    }

    private JFrame getMainWindow() {
        JFrame mainWindow = new JFrame("迷宫生成器");

        mainWindow.setSize(600, 800);
        mainWindow.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 400);
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

        return _imageLabel;
    }

    private JLabel setUpWidthLabel() {
        JLabel widthLabel = new JLabel("宽度：");
        widthLabel.setBounds(25, 600, 50, 30);

        return widthLabel;
    }

    private JTextField setupWidthText() {
        _widthText = new JTextField();
        _widthText.setBounds(75, 600, 100, 30);
        _widthText.setText(_mazeWidth + "");

        _widthText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    _mazeWidth = Integer.parseInt(_widthText.getText());
                } catch (Exception e2) {
                    _widthText.setText(_mazeWidth + "");
                }
            }
        });

        return _widthText;
    }

    private JLabel setUpHeightLabel() {
        JLabel heightLabel = new JLabel("高度：");
        heightLabel.setBounds(225, 600, 50, 30);

        return heightLabel;
    }

    private JTextField setupHeightText() {
        _heightText = new JTextField();
        _heightText.setBounds(275, 600, 100, 30);
        _heightText.setText(_mazeHeight + "");

        _heightText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    _mazeHeight = Integer.parseInt(_heightText.getText());
                } catch (Exception e2) {
                    _heightText.setText(_mazeHeight + "");
                }
            }
        });

        return _heightText;
    }

    private JLabel setUpScaleLabel() {
        JLabel scaleLabel = new JLabel("缩放：");
        scaleLabel.setBounds(425, 600, 50, 30);

        return scaleLabel;
    }

    private JTextField setupScaleText() {
        _scaleText = new JTextField();
        _scaleText.setBounds(475, 600, 100, 30);
        _scaleText.setText(_mazeImageScale + "");

        _scaleText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    _mazeImageScale = Integer.parseInt(_scaleText.getText());
                } catch (Exception e2) {
                    _scaleText.setText(_mazeImageScale + "");
                }
            }
        });

        return _scaleText;
    }

    private JButton setupSpownButton() {
        _spownButton = new JButton("生成迷宫");
        _spownButton.setBounds(50, 650, 200, 50);
        _spownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("生成按钮按下");
                setupSpownThread();

                Spowner mazeSpowner = new Spowner(_mazeWidth, _mazeHeight);
                _spownThread = new Thread(mazeSpowner);
                _spownThread.start();

                while (_maze == null) {
                    Thread.yield(); // 暂停线程是因为主线程需要等待生成线程创建迷宫，占着执行权也没用，不如释放出去增加生成线程抢到执行权的机会
                    _maze = mazeSpowner.maze(); // 生成线程启动后主线程有可能立即抢回执行权，丝毫不减速的继续执行，很可能获取的时候生成线程还没有创建出地图，所以要一直循环直到获取到地图                
                }

                _displayThread = new Thread(new SpownDisplayer(_maze, _imageLabel, _spownThread));
                _displayThread.start();
            }

            private void setupSpownThread() {
                _maze = null; // 后续操作根据地图是否为null判断是否获取到了新生成的地图，所以这里要先把地图改为null
                if (_displayThread != null)
                    _displayThread.interrupt();
            }
        });

        _spownButton.getActionListeners()[0].actionPerformed(null);

        return _spownButton;
    }

    private JButton setupSaveButton() {
        _saveButton = new JButton("保存地图");
        _saveButton.setBounds(320, 650, 200, 50);
        _saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SaveMaze.saveImage(SaveMaze.mazeToImage(_maze), "迷宫", _mazeImageScale);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        return _saveButton;
    }
}

class Spowner implements Runnable {
    int _width;
    int _height;
    MazeSpowner _mazeSpowner = null;

    public Spowner(int width, int height) {
        _width = width;
        _height = height;
        _mazeSpowner = new MazeSpowner();
    }

    public Maze maze() {
        return _mazeSpowner.maze();
    }

    @Override
    public void run() {
        _mazeSpowner.spown(_width, _height);
    }
}

class SpownDisplayer implements Runnable {
    Maze _maze;
    JLabel _mazeImageLabel;
    Thread _spownThread;

    public SpownDisplayer(Maze maze, JLabel mazeImage, Thread spownThread) {
        _maze = maze;
        _mazeImageLabel = mazeImage;
        _spownThread = spownThread;
    }

    @Override
    public void run() {
        while (true) {
            if (!_spownThread.isAlive() || Thread.currentThread().isInterrupted())
                break;
            setMazeImageToImageLabel();
        }

        setMazeImageToImageLabel();
    }

    private void setMazeImageToImageLabel() {
        Image scaledMazeImage;
        if (_maze.getWidth() > _maze.getHeight())
            scaledMazeImage = SaveMaze.mazeToImage(_maze).getScaledInstance(550, -1, Image.SCALE_FAST);
        else
            scaledMazeImage = SaveMaze.mazeToImage(_maze).getScaledInstance(-1, 550, Image.SCALE_FAST);

        _mazeImageLabel.setIcon(new ImageIcon(scaledMazeImage));
    }
}
