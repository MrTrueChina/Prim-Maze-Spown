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
        System.out.println("生成总时间 = " + (System.currentTimeMillis() - startSpownTime) + "毫秒");

        //SaveMazeAsPicture.saveMaze(maze, 3, "迷宫");

        System.out.println("生成完成");
    }
}
