package maze;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

public class MazeSpowner {
    private static LinkedList<Point> _readyNodes;
    private static LinkedList<Point> _carvedNodes;
    private static Maze _maze;

    private enum NodeState {
        DEACTIVE, READY_CARVE, CARVED,
    }

    private static NodeState[][] _nodes;

    private MazeSpowner() {
    }

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
        int mazeWidth = width % 2 != 0 ? width : width + 1;
        int mazeHeight = height % 2 != 0 ? height : height + 1;

        _maze = new Maze(mazeWidth, mazeHeight);
        _readyNodes = new LinkedList<Point>();
        _carvedNodes = new LinkedList<Point>();

        _nodes = new NodeState[mazeWidth / 2][mazeHeight / 2];
        for (int y = 0; y < _nodes[0].length; y++)
            for (int x = 0; x < _nodes.length; x++)
                _nodes[x][y] = NodeState.DEACTIVE;
    }

    private static void cleanSpowner() {
        _maze = null;
        _readyNodes = null;
        _carvedNodes = null;
        _nodes = null;
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
        carveStartNode();
        System.out.println("开起点耗时 " + (System.currentTimeMillis() - startOpenStartNodeTime) + " 毫秒");

        while (_readyNodes.size() > 0) {
            long startCarveNodeTime = System.currentTimeMillis();
            carveRandomNode();
            System.out.println("雕刻一个节点耗时 " + (System.currentTimeMillis() - startCarveNodeTime) + " 毫秒");
        }
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

        carved(carvedNode, readyNode);
        _readyNodes.remove(readyNode);
        _carvedNodes.add(readyNode);
    }

    private static void carveStartNode() {
        /*
         * 启动
         * 
         * 1、找一个没有雕刻的点
         * 2、这个点加入闭表，作为已雕刻的点的主体
         * 3、这个点相邻的点加入开表
         */
        Point startNode = getUncarvedNode();
        _carvedNodes.add(startNode);
        if (_maze.contains(startNode.x, startNode.y + 2))
            _readyNodes.add(new Point(startNode.x, startNode.y + 2));
        if (_maze.contains(startNode.x + 2, startNode.y))
            _readyNodes.add(new Point(startNode.x + 2, startNode.y));
        if (_maze.contains(startNode.x, startNode.y - 2))
            _readyNodes.add(new Point(startNode.x, startNode.y - 2));
        if (_maze.contains(startNode.x - 2, startNode.y))
            _readyNodes.add(new Point(startNode.x - 2, startNode.y));
    }

    private static Point getUncarvedNode() {
        for (int y = 0; y < _maze.height; y++)
            for (int x = 0; x < _maze.width; x++)
                if (_maze.getPassable(x, y))
                    return new Point(x, y);
        return null;
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

    private static void carved(Point startPoint, Point targetPoint) {
        _maze.setPassable(true, (startPoint.x + targetPoint.x) / 2, (startPoint.y + targetPoint.y) / 2);

        LinkedList<Point> contiguousNode = getContiguousNodes(targetPoint);

        for (Point point : contiguousNode)
            if (!_readyNodes.contains(point) && !_carvedNodes.contains(point))
                _readyNodes.add(point);
    }
}
