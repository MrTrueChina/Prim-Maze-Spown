package display;

import java.io.FileNotFoundException;
import java.io.IOException;

import maze.Maze;
import maze.MazeSpowner;
import save.SaveMazeAsPicture;

public class MainWindow {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        long startSpownTime = System.currentTimeMillis();
        Maze maze = MazeSpowner.spown(100, 100);
        System.out.println("������ʱ�� = " + (System.currentTimeMillis() - startSpownTime) + "����");

        //SaveMazeAsPicture.saveMaze(maze, 3, "�Թ�");

        System.out.println("�������");
    }
}
