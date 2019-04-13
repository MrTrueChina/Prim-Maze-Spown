package maze;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

public class MazeSpowner {
    private static LinkedList<Point> _readyNodes;
    private static LinkedList<Point> _carvedNodes;
    private static Maze _maze;

    /**
     * 按指定尺寸生成迷宫，当尺寸为双数时自动+1
     * 
     * @param width  迷宫的宽度
     * @param height 迷宫的高度
     * @return
     */
    public static Maze spown(final int width, final int height) {
        try {
            setupSpowner(width, height);
            doSpown();
            return _maze;
        } finally {
            cleanSpowner();
        }
    }

    private static void setupSpowner(final int width, final int height) {
        _maze = new Maze(width % 2 != 0 ? width : width + 1, height % 2 != 0 ? height : height + 1);
        _readyNodes = new LinkedList<Point>();
        _carvedNodes = new LinkedList<Point>();
    }

    private static void cleanSpowner() {
        _maze = null;
        _readyNodes = null;
        _carvedNodes = null;
    }

    private static void doSpown() {
        long startSetupMazeTime = System.currentTimeMillis();
        setupMaze();
        System.out.println("初始化迷宫耗时 " + (System.currentTimeMillis() - startSetupMazeTime) + " 毫秒");

        long startCarveMazeTime = System.currentTimeMillis();
        carveMaze();
        System.out.println("雕刻迷宫耗时 " + (System.currentTimeMillis() - startCarveMazeTime) + " 毫秒");
    }

    private static void setupMaze() {
        for (int x = 1; x < _maze.width; x++)
            for (int y = 1; y < _maze.height; y++)
                _maze.setPassable(false, x, y);

        for (int x = 1; x < _maze.width; x += 2)
            for (int y = 1; y < _maze.height; y += 2)
                _maze.setPassable(true, x, y);
    }

    private static void carveMaze() {
        long startOpenStartNodeTime = System.currentTimeMillis();
        openStartNode();
        System.out.println("开起点耗时 " + (System.currentTimeMillis() - startOpenStartNodeTime) + " 毫秒");
        System.out.println("开起点后开表内节点数 = " + _readyNodes.size());

        while (_readyNodes.size() > 0) {
            long startCarveRandomNodeTime = System.currentTimeMillis();
            carveRandomNode();
            System.out.println("随机雕刻一个节点耗时 " + (System.currentTimeMillis() - startCarveRandomNodeTime) + " 毫秒");
        }
    }

    private static void openStartNode() {
        Point startNode = new Point(1, 1); // 以左上角的洞作为起点

        carve(startNode, startNode);
    }

    private static void carveRandomNode() {
        /*
         * 雕刻一个节点
         * 
         * 0、根据普里姆算法，每次在开表里找一个到最近的闭表点距离最近的点，而这里所有点是等距的，所以是随机找一个开表的点再连接到随机一个相邻点
         * 1、在开表里随机找出一个点
         * 2、在这个点的周围找出所有在闭表里的点
         * 3、在这些点里随机找一个打通
         * 4、这个点加入闭表
         * 5、这个点周围的点加入开表
         */
        Point readyNode = getRandomReadyNode();
        Point carvedNode = getRandomContiguousCarvedNode(readyNode);

        carve(carvedNode, readyNode);
    }

    private static Point getRandomReadyNode() {
        return getRandomNode(_readyNodes);
    }

    /**
     * 随机获取一个指定节点相邻的已经雕刻的节点
     * 
     * @param centerPoint 要获取相邻节点的节点
     * @return 如果存在相邻的已雕刻节点，则返回一个已雕刻节点，否则返回null
     */
    private static Point getRandomContiguousCarvedNode(final Point centerPoint) {
        LinkedList<Point> nodes = getContiguousNodes(centerPoint);

        Iterator<Point> nodesIterator = nodes.iterator();
        while (nodesIterator.hasNext())
            if (!_carvedNodes.contains(nodesIterator.next()))
                nodesIterator.remove(); // 通过迭代器移除元素，这个方法必须在 next() 之后调用，并且只能移除刚刚通过 next() 获取的元素

        if (nodes.size() > 0)
            return getRandomNode(nodes);
        return null;
    }

    private static Point getRandomNode(LinkedList<Point> nodes) {
        if (nodes.size() != 0)
            return nodes.get(random(nodes.size()));
        return null;
    }

    private static int random(int bound) {
        return (int) (Math.random() * bound);
    }

    private static LinkedList<Point> getContiguousNodes(final Point centerPoint) {
        LinkedList<Point> nodes = new LinkedList<Point>();

        if (_maze.contains(centerPoint.x, centerPoint.y + 2))
            nodes.add(new Point(centerPoint.x, centerPoint.y + 2));
        if (_maze.contains(centerPoint.x + 2, centerPoint.y))
            nodes.add(new Point(centerPoint.x + 2, centerPoint.y));
        if (_maze.contains(centerPoint.x, centerPoint.y - 2))
            nodes.add(new Point(centerPoint.x, centerPoint.y - 2));
        if (_maze.contains(centerPoint.x - 2, centerPoint.y))
            nodes.add(new Point(centerPoint.x - 2, centerPoint.y));

        return nodes;
    }

    private static void carve(Point startNode, Point targetNode) {
        breakWall(startNode, targetNode);
        setContiguousDeactiveNodeToReadyList(targetNode);
        moveNodeToCarved(targetNode);
    }

    private static void breakWall(Point startNode, Point targetNode) {
        System.out.println("startNode = " + startNode);
        System.out.println("targetNode = " + targetNode);
        _maze.setPassable(true, (startNode.x + targetNode.x) / 2, (startNode.y + targetNode.y) / 2);
    }

    private static void setContiguousDeactiveNodeToReadyList(Point centerNode) {
        LinkedList<Point> contiguousNode = getContiguousNodes(centerNode);
        System.out.println("相邻节点数量 = " + contiguousNode.size());

        for (Point point : contiguousNode)
            if (!_readyNodes.contains(point) && !_carvedNodes.contains(point))
                _readyNodes.add(point);
    }

    private static void moveNodeToCarved(Point node) {
        _readyNodes.remove(node);
        _carvedNodes.add(node);
    }
}
